package r.evgenymotorin.recipes.di

import android.content.Context
import dagger.Module
import dagger.Provides
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.interfaces.RecipeDataDao
import javax.inject.Singleton

@Module
class DatabaseUtilsModule(val context: Context) {

    @Provides
    @Singleton
    fun providesDatabase(): RecipesDataBase {
        return RecipesDataBase.getInstance(context)!!
    }

    @Provides
    @Singleton
    fun providesRecipeDataDao(db: RecipesDataBase): RecipeDataDao {
        return db.RecipeDataDao()
    }
}