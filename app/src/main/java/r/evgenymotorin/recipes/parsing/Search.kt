package r.evgenymotorin.recipes.parsing

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.database.interfaces.RecipeDataDao
import r.evgenymotorin.recipes.rows.RecipeRow
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class Search(private val dbHelper: RecipesDataBaseHelper) {
    private var isNowLoadingData = false

    fun searchPostsInToRow(adapter: GroupAdapter<ViewHolder>, url: String, progress: ProgressBar, firstProgress: ProgressBar) {
        isNowLoadingData = true

        if (adapter.itemCount == 0) firstProgress.visibility = View.VISIBLE
        else progress.visibility = View.VISIBLE

        Query().querySearchElements(url)
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
                    Single.fromCallable { Parsers().scrapPostFromHTML(element) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe {
                            adapter.add(RecipeRow(it))
                            dbHelper.db.RecipeDataDao().insert(it)


                        }
                } else {
                    Log.d("searching_log", "url: $postUrl")
                    adapter.add(RecipeRow(recipe))
                }
            }
            .doOnCompleted {
                isNowLoadingData = false
                progress.visibility = View.INVISIBLE
                firstProgress.visibility = View.GONE
            }
            .subscribe {}
    }

    fun isNowLoadingData(): Boolean {
        return isNowLoadingData
    }
}