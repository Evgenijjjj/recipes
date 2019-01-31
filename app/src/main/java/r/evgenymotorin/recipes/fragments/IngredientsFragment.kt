package r.evgenymotorin.recipes.fragments

import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.tables.AdaptiveIngredientData
import r.evgenymotorin.recipes.db.tables.RecipeData
import r.evgenymotorin.recipes.di.base.BaseFragment
import r.evgenymotorin.recipes.rows.HeaderRow
import r.evgenymotorin.recipes.rows.IngredientRow

class IngredientsFragment: BaseFragment() {
    private val adapter = GroupAdapter<ViewHolder>()
    private var recipeData: RecipeData? = null
    private var adaptiveIngredientDataList: List<AdaptiveIngredientData>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.ingredients_recipe_fragment, container, false)
        v.findViewById<RecyclerView>(R.id.ingredients_recycler_view).adapter = adapter
        return v
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val recipeId = arguments?.getInt(getString(R.string.recipeDataId))

            if (recipeId == null) {
                Toast.makeText(activity, "recipeID == null", Toast.LENGTH_LONG).show()
                return
            }

            recipeData = db.RecipeDataDao().getRecipeWithId(recipeId)
            adaptiveIngredientDataList = dbHelper.getAllAdaptiveIngredientsData(recipeData!!)

            if (recipeData!!.adaptiveIngredientFlag == 1) {
                adapter.add(HeaderRow(recipeData!!.adaptiveIngredientsTitle!!))
                for (ingredient in adaptiveIngredientDataList!!)
                    adapter.add(IngredientRow(ingredient))
            } else{
                val text = recipeData!!.notAdaptiveIngredientsText ?: activity?.getString(R.string.listIsEmpty)
                adapter.add(HeaderRow(text))
            }

            adapter.setOnItemClickListener { item, _ ->
                try {
                    val row = item as IngredientRow

                    if (!row.additionalInfo.isNullOrEmpty()) {
                        popupWindow.contentView.findViewById<TextView>(R.id.text_view_popup_window).text =
                                row.additionalInfo
                        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0)
                    }
                } catch (e: Exception) {
                }
            }
        } catch (e: Exception) {
            Log.d("DSvbfgfh", e.toString())
        }
    }

}