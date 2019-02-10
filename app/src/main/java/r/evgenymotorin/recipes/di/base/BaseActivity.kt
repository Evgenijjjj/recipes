package r.evgenymotorin.recipes.di.base

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.di.modules.NetworkModule
import r.evgenymotorin.recipes.parsing.Search
import rx.Single
import java.net.URLDecoder
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    val Activity.app : RecipesApp
    get() = application as RecipesApp

    private val component by lazy { app.component.addComponent(NetworkModule(this)) }

    @Inject lateinit var internetConnection: Single<Boolean>
    @Inject lateinit var db: RecipesDataBase
    @Inject lateinit var dbHelper: RecipesDataBaseHelper
    @Inject lateinit var actualRecipesActivityIntent: Intent
    @Inject lateinit var defaultSharedPreferences: SharedPreferences
    @Inject lateinit var postsSearch: Search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    fun getCurrentPeriod(): String {
        var currentPeriod = URLDecoder.decode(defaultSharedPreferences.getString(getString(R.string.filter_period_key), getString(
            R.string.default_value_period)), "UTF-8")
        currentPeriod = currentPeriod.replaceFirst("р", "Р")
        currentPeriod = currentPeriod.replace(" ", "ы ")
        return currentPeriod
    }
}