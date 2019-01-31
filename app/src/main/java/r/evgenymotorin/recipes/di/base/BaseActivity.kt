package r.evgenymotorin.recipes.di.base

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.di.modules.NetworkModule
import rx.Single
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    val Activity.app : RecipesApp
    get() = application as RecipesApp

    val component by lazy { app.component.addComponent(NetworkModule(this)) }

    @Inject lateinit var internetConnection: Single<Boolean>
    @Inject lateinit var db: RecipesDataBase
    @Inject lateinit var dbHelper: RecipesDataBaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }
}