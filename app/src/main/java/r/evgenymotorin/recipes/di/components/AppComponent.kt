package r.evgenymotorin.recipes.di.components

import dagger.Component
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.di.modules.NetworkModule
import r.evgenymotorin.recipes.di.modules.AppModule
import r.evgenymotorin.recipes.di.modules.FragmentModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: RecipesApp)

    fun addComponent(mainModule: NetworkModule): NetworkComponent

    fun addComponent(fragmentModule: FragmentModule): FragmentComponent

    //fun addComponent(databaseUtilsModule: DatabaseUtilsModule): DatabaseUtilsSubComponent
}