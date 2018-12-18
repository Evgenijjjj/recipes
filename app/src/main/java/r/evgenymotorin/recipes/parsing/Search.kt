package r.evgenymotorin.recipes.parsing

import android.util.Log
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.fragments.SearchFragment.Companion.isNowLoadingData
import r.evgenymotorin.recipes.rows.RecipeRow
import java.net.URLEncoder
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class Search {
    fun searchPostsInToRow(adapter: GroupAdapter<ViewHolder>, searchKey: String, pageNum: Int) {
        var encodedKey = URLEncoder.encode(searchKey, "utf-8")
        Log.d("search_log", "key: $encodedKey")
        val url = "https://www.edimdoma.ru/retsepty?direction=&field=&page=$pageNum&query=$encodedKey&user_ids=&with_ingredient=&without_ingredient="

        Query().querySearchElements(url)
            .flatMap { urls -> Observable.from(urls) }
            .doOnNext { element ->
                Single.fromCallable { Parsers().scrapPostFromHTML(element) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.add(RecipeRow(it)) }
            }
            .doOnCompleted { isNowLoadingData = false }
            .subscribe {}

    }
}