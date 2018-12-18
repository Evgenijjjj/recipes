package r.evgenymotorin.recipes.di.app

import dagger.Component
import r.evgenymotorin.recipes.di.activity.MainComponent
import r.evgenymotorin.recipes.di.activity.MainModule
import r.evgenymotorin.recipes.di.fragment.FragmentComponent
import r.evgenymotorin.recipes.di.fragment.FragmentModule
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(AppModule::class))
interface AppComponent {
    fun inject(app: RecipesApp)

    fun addComponent(mainModule: MainModule): MainComponent

    fun addComponent(fragmentModule: FragmentModule): FragmentComponent
}