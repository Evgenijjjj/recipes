package r.evgenymotorin.recipes.di.activity

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import r.evgenymotorin.recipes.di.app.RecipesApp
import rx.Single
import javax.inject.Inject

open class BaseActivity: AppCompatActivity() {
    val Activity.app : RecipesApp
    get() = application as RecipesApp

    val component by lazy { app.component.addComponent(MainModule(this)) }

    @Inject lateinit var internetConnection: Single<Boolean>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }
}