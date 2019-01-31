package r.evgenymotorin.recipes.di.modules

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.PopupWindow
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import dagger.Module
import dagger.Provides
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.parsing.Search
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

    @Provides
    @Singleton
    fun providesPopupWindow(): PopupWindow {
        val popupView = activity.layoutInflater.inflate(R.layout.popup_window, null)
        val popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true)
        popupView.setOnTouchListener { _, _ ->
            popupWindow.dismiss()
            true
        }

        return  popupWindow
    }

    @Provides
    @Singleton
    fun providesPostsSearch(dbHelper: RecipesDataBaseHelper): Search {
        return Search(dbHelper)
    }

   /*@Provides
    @Singleton
    fun providesAdapter(): GroupAdapter<ViewHolder> {
        return GroupAdapter<ViewHolder>()
    }*/
}