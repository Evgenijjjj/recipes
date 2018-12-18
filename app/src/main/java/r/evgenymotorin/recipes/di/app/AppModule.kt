package r.evgenymotorin.recipes.di.app

import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: RecipesApp) {
    @Provides
    @Singleton
    fun provideApp() = app
}