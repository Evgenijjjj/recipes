package r.evgenymotorin.recipes.di.activity

import android.content.Context
import android.support.v4.app.FragmentManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class MainModule(val context: Context){

    @Provides
    @Singleton
    fun sharedPreferences() = context.getSharedPreferences("test", Context.MODE_PRIVATE)!!

}