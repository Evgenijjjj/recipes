package r.evgenymotorin.recipes.di.base

import android.app.Activity
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import android.widget.PopupWindow
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.RecipesDataBase
import r.evgenymotorin.recipes.database.RecipesDataBaseHelper
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.di.modules.FragmentModule
import r.evgenymotorin.recipes.parsing.Search
import java.net.URLDecoder
import javax.inject.Inject

open class BaseFragment: Fragment() {

    val Activity.app : RecipesApp
        get() = application as RecipesApp

    private val component by lazy { activity?.app?.component?.addComponent(
        FragmentModule(
            activity!!
        )
    ) }

    @Inject lateinit var inputMethodManager: InputMethodManager
    @Inject lateinit var linearLayoutManager: LinearLayoutManager
    @Inject lateinit var defaultPostBitmap: Bitmap
    @Inject lateinit var popupWindow: PopupWindow
    @Inject lateinit var postsSearch: Search
    @Inject lateinit var db: RecipesDataBase
    @Inject lateinit var dbHelper: RecipesDataBaseHelper
    @Inject lateinit var defaultSharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component?.inject(this)
    }

    fun getCurrentPeriod(): String {
        var currentPeriod = URLDecoder.decode(defaultSharedPreferences.getString(getString(R.string.filter_period_key), getString(
            R.string.default_value_period)), "UTF-8")
        currentPeriod = currentPeriod.replaceFirst("р", "Р")
        currentPeriod = currentPeriod.replace(" ", "ы ")
        return currentPeriod
    }
}