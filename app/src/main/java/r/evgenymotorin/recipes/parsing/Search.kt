package r.evgenymotorin.recipes.parsing

import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.rows.RecipeRow
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers


class Search {
    private var isNowLoadingData = false

    fun searchPostsInToRow(adapter: GroupAdapter<ViewHolder>, url: String, progress: ProgressBar) {
        isNowLoadingData = true

        progress.visibility = View.VISIBLE

        Query().querySearchElements(url)
            .flatMap { urls -> Observable.from(urls) }
            .doOnNext { element ->
                Single.fromCallable { Parsers().scrapPostFromHTML(element) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { adapter.add(RecipeRow(it)) }
            }
            .doOnCompleted {
                isNowLoadingData = false
                progress.visibility = View.INVISIBLE
            }
            .subscribe {}
    }

    fun isNowLoadingData(): Boolean {
        return isNowLoadingData
    }
}