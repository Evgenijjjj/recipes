package r.evgenymotorin.recipes.di.components

import dagger.Subcomponent
import r.evgenymotorin.recipes.di.base.BaseActivity
import r.evgenymotorin.recipes.di.modules.NetworkModule

@Subcomponent(modules = arrayOf(NetworkModule::class))
interface NetworkComponent {
    fun inject(baseActivity: BaseActivity)



    //fun plus(databaseUtilsModule: DatabaseUtilsModule): DatabaseUtilsSubComponent
}