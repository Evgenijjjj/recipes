package r.evgenymotorin.recipes.di.app

import android.app.Application
import r.evgenymotorin.recipes.di.components.AppComponent
import r.evgenymotorin.recipes.di.components.DaggerAppComponent
import r.evgenymotorin.recipes.di.modules.AppModule

class RecipesApp : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        component.inject(this)
    }
}