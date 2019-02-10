package r.evgenymotorin.recipes.di.modules

import android.content.Intent
import android.os.Build
import android.preference.PreferenceManager
import android.support.annotation.RequiresApi
import dagger.Module
import dagger.Provides
import r.evgenymotorin.recipes.ActualRecipesActivity
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.parsing.Search
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

    @Provides
    @Singleton
    fun provideActualRecipesActivityIntent(): Intent {
        val i = Intent(app, ActualRecipesActivity::class.java)
        i.flags = Intent.FLAG_ACTIVITY_NO_ANIMATION
        return i
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @Provides
    @Singleton
    fun provideDefaultSharedPreferences() = PreferenceManager.getDefaultSharedPreferences(app)!!

    @Provides
    @Singleton
    fun providesPostsSearch(dbHelper: RecipesDataBaseHelper): Search {
        return Search(dbHelper)
    }
}