package r.evgenymotorin.recipes.di.components

import dagger.Subcomponent
import r.evgenymotorin.recipes.di.base.BaseFragment
import r.evgenymotorin.recipes.di.modules.FragmentModule
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(baseFragment: BaseFragment)
}