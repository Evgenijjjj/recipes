package r.evgenymotorin.recipes.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.model.Ingredients
import r.evgenymotorin.recipes.parsing.Parsers
import r.evgenymotorin.recipes.rows.HeaderRow
import r.evgenymotorin.recipes.rows.IngredientRow

class IngredientsFragment: BaseFragment() {
    private val adapter = GroupAdapter<ViewHolder>()
    private var ingredients: Ingredients? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.ingredients_recipe_fragment, container, false)
        v.findViewById<RecyclerView>(R.id.ingredients_recycler_view).adapter = adapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (RecipeActivity.pageHTML == null) {
            Toast.makeText(activity, "page == null", Toast.LENGTH_LONG).show()
            return
        }

        ingredients = Parsers().scrapIngredientsFromHTML(RecipeActivity.pageHTML!!)

        if (ingredients!!.isAdaptive) {
            adapter.add(HeaderRow(ingredients!!.title!!))
            for (ingredient in ingredients!!.adaptiveIngredients!!)
                adapter.add(IngredientRow(ingredient))
        } else {
            adapter.add(HeaderRow(ingredients!!.text!!))
        }
    }

}