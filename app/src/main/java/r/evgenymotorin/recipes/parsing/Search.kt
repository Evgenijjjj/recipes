package r.evgenymotorin.recipes.parsing

import android.os.Handler
import android.util.Log
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.rows.LoadingRow
import r.evgenymotorin.recipes.rows.NothingFoundRow
import r.evgenymotorin.recipes.rows.RecipeRow
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class Search(private val dbHelper: RecipesDataBaseHelper) {
    private var isNowLoadingData = false

    fun searchPostsInToRow(adapter: GroupAdapter<ViewHolder>, url: String) {
        isNowLoadingData = true

        var loadingItemFlag = true
        val loadingGroup: LoadingRow = if (adapter.itemCount == 0) LoadingRow(false) else LoadingRow(true)
        adapter.add(loadingGroup)

        Query().querySearchElements(url)
            .flatMap { urls -> if (urls.isEmpty()) { adapter.add((NothingFoundRow())) };Observable.from(urls) }
            .doOnNext { element ->
                Log.d("fsvsdvsvsd", element.toString())
                val postUrl = "https://www.edimdoma.ru" + element
                    .select("a[href]")[0]
                    .attr("href")

                val recipe = dbHelper.db.RecipeDataDao().getRecipeWithUrl(postUrl)
                /**
                 * checking for the presence of this recipe in the database
                 */
                if (recipe == null) {
                    Single.fromCallable { Parsers().scrapPostFromHTML(element) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (loadingItemFlag) {
                                adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                                loadingItemFlag = false
                            }
                            adapter.add(RecipeRow(it, false))
                            dbHelper.db.RecipeDataDao().insert(it)
                        }
                } else {
                    if (loadingItemFlag) {
                        adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                        loadingItemFlag = false
                    }
                    Log.d("searching_log", "url: $postUrl")
                    val isFavourite = dbHelper.db.FavouritesDataDao().getFavouritesDataWithUrl(recipe.recipeUrl) != null
                    adapter.add(RecipeRow(recipe, isFavourite))
                }
            }
            .doOnCompleted {
                isNowLoadingData = false

                if (loadingItemFlag) {
                    adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                    loadingItemFlag = false
                }
            }
            .subscribe {}
    }

    fun searchSpecialPostsInToRow(adapter: GroupAdapter<ViewHolder>, url: String) {
        isNowLoadingData = true
        var loadingItemFlag = true
        val loadingGroup: LoadingRow = if (adapter.itemCount == 0) LoadingRow(false) else LoadingRow(true)


        adapter.add(loadingGroup)

        Query().querySearchSpecialElements(url)
            .flatMap { urls -> Observable.from(urls) }
            .doOnNext { element ->
                val postUrl = "https://www.edimdoma.ru" + element
                    .select("a[href]")[0]
                    .attr("href")

                val recipe = dbHelper.db.RecipeDataDao().getRecipeWithUrl(postUrl)
                /**
                 * checking for the presence of this recipe in the database
                 */
                if (recipe == null) {
                    Single.fromCallable { Parsers().scrapSpecialPostFromHTML(element) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            if (loadingItemFlag) {
                                adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                                loadingItemFlag = false
                            }
                            adapter.add(RecipeRow(it, false))
                            dbHelper.db.RecipeDataDao().insert(it)
                        }
                } else {
                    if (loadingItemFlag) {
                        adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                        loadingItemFlag = false
                    }

                    Log.d("searching_log", "url: $postUrl")
                    val isFavourite = dbHelper.db.FavouritesDataDao().getFavouritesDataWithUrl(recipe.recipeUrl) != null
                    adapter.add(RecipeRow(recipe, isFavourite))
                }
            }
            .doOnCompleted {
                isNowLoadingData = false

                if (loadingItemFlag) {
                    adapter.removeGroup(adapter.getAdapterPosition(loadingGroup))
                    loadingItemFlag = false
                }
            }
            .subscribe {}
    }

    fun isNowLoadingData(): Boolean {
        return isNowLoadingData
    }

    fun reload() {
        isNowLoadingData = false
    }
}