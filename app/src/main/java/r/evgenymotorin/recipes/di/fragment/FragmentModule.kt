package r.evgenymotorin.recipes.di.fragment

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import dagger.Module
import dagger.Provides
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.di.activity.BaseActivity
import javax.inject.Singleton

@Module
class FragmentModule(private val activity: FragmentActivity) {

    @Singleton
    @Provides
    fun providesContext() : Context {
        return activity
    }

    @Singleton
    @Provides
    fun providesInputMethodManager(): InputMethodManager {
        return activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    @Singleton
    @Provides
    fun providesLinearLayoutManager(): LinearLayoutManager {
        return LinearLayoutManager(activity)
    }

    @Provides
    fun providesDefaultPostBitmap(): Bitmap {
        return BitmapFactory.decodeResource(activity.resources, R.drawable.food)
    }

    @Provides @Singleton fun providesSupportFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }
}