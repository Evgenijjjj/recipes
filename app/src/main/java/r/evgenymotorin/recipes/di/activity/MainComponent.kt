package r.evgenymotorin.recipes.di.activity

import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(MainModule::class))
interface MainComponent {
    fun inject(mainActivity: BaseActivity)
}