package r.evgenymotorin.recipes.parsing

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.db.tables.RecipeData
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

    fun scrapDetailedInfoForRecipe(dbHelper: RecipesDataBaseHelper, recipeData: RecipeData): Single<Unit> {
        return Single.fromCallable {
            Parsers().scrapDetailedInformationForRecipeData(dbHelper, recipeData)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    /*fun saveRecipeToDataBase(db: RecipesDataBase, recipePage: Document): Single<Boolean> {
        return Single.fromCallable {
            Parsers().saveRecipeToDB(db, recipePage)
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }*/
}