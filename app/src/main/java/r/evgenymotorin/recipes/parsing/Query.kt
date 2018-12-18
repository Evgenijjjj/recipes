package r.evgenymotorin.recipes.parsing

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import rx.Observable
import rx.Single
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class Query {
    fun querySearchElements(url: String): Observable<List<Element>> {
        return Observable.create(Observable.OnSubscribe<List<Element>> { subscriber ->
            subscriber.onNext(Parsers().findPosts(url))
            subscriber.onCompleted()
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
    
    fun downloadPage(url: String): Single<Document> {
        return Single.fromCallable {
            Jsoup.connect(url).get()
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }
}