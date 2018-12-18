package r.evgenymotorin.recipes.di.fragment

import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {
    fun inject(baseFragment: BaseFragment)
}