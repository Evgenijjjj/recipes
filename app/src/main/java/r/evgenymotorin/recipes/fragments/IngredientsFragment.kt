package r.evgenymotorin.recipes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.di.fragment.BaseFragment

class IngredientsFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.ingredients_recipe_fragment, container, false)
    }
}