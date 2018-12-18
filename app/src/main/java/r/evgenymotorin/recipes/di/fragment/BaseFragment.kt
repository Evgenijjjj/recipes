package r.evgenymotorin.recipes.di.fragment

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import r.evgenymotorin.recipes.di.app.RecipesApp
import r.evgenymotorin.recipes.parsing.Parsers
import javax.inject.Inject

open class BaseFragment: Fragment() {

    val Activity.app : RecipesApp
        get() = application as RecipesApp

    val component by lazy { activity?.app?.component?.addComponent(FragmentModule(activity!!)) }

    @Inject lateinit var inputMethodManager: InputMethodManager
    @Inject lateinit var linearLayoutManager: LinearLayoutManager
    @Inject lateinit var defaultPostBitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component?.inject(this)
    }
}