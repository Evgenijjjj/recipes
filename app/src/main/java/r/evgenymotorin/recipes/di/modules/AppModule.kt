package r.evgenymotorin.recipes.di.modules

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.di.app.RecipesApp
import javax.inject.Singleton

@Module
class AppModule(val app: RecipesApp) {
    @Provides
    @Singleton
    fun provideApp() = app

    @Provides
    @Singleton
    fun provideDataBase(): RecipesDataBase {
        return RecipesDataBase.getInstance(app)!!
    }

    @Provides
    @Singleton
    fun provideDataBaseHelper(db: RecipesDataBase): RecipesDataBaseHelper {
        return RecipesDataBaseHelper(db)
    }
}