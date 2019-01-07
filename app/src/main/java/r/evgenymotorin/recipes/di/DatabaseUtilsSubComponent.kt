package r.evgenymotorin.recipes.di

import dagger.Subcomponent
import r.evgenymotorin.recipes.database.interfaces.RecipeDataDao
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = arrayOf(DatabaseUtilsModule::class))
interface DatabaseUtilsSubComponent {

    val recipeDataDao: RecipeDataDao

    @Subcomponent.Builder
    interface builder {
        fun build(): DatabaseUtilsSubComponent
    }
    //fun inject(context: Context)
    //fun inject(baseFragment: BaseFragment)
}