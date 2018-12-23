package r.evgenymotorin.recipes.rows

import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.ingredient_row.view.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.model.AdaptiveIngredient

class IngredientRow(private val ingredient: AdaptiveIngredient): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.ingredient_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.count_ingredients_row.text = ingredient.count
        viewHolder.itemView.ingredient_textview_ingredients_row.text = ingredient.ingredient

        if (ingredient.aboutIngredient != null) {
            viewHolder.itemView.info_ingredient_row.visibility = View.VISIBLE
        } else
            viewHolder.itemView.info_ingredient_row.visibility = View.INVISIBLE
    }
}