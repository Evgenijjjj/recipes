package r.evgenymotorin.recipes.model

import android.view.View
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.rows.RecipeRow

class ClickedRecipe {
    lateinit var row: RecipeRow
    lateinit var group: Item<ViewHolder>
    var position: Int? = null
    lateinit var view: View
}