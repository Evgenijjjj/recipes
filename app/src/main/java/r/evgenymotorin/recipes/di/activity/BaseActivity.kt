package r.evgenymotorin.recipes.di.activity

import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import r.evgenymotorin.recipes.di.app.RecipesApp
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    val Activity.app : RecipesApp
    get() = application as RecipesApp

    val component by lazy { app.component.addComponent(MainModule(this)) }

    @Inject lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }
}