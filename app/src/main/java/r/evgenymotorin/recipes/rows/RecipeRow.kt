package r.evgenymotorin.recipes.rows

import android.util.Log
import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recipe_row.view.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.fragments.SEARCH_FRAGMENT_LOG
import r.evgenymotorin.recipes.fragments.SearchFragment.Companion.defPostBitmap
import r.evgenymotorin.recipes.model.Post

class RecipeRow(private val post: Post): Item<ViewHolder>() {
    var recipeLink = ""
    var recipeName = ""

    override fun getLayout(): Int {
        return R.layout.recipe_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name_recipe_row.text = post.recipeName

        recipeLink = post.recipeUrl
        recipeName = post.recipeName

        viewHolder.itemView.number_of_servings_recipe_row.text = post.numberOfServings
        viewHolder.itemView.time_recipe_row.text = post.cookingTime
        viewHolder.itemView.ingredients_text_view_recipe_row.text = post.ingredients

        if (post.numberOfServings.isEmpty()) {
            viewHolder.itemView.img_number_of_servings_recipe_row.alpha = 0.2f
            viewHolder.itemView.number_of_servings_recipe_row.alpha = 0.2f
        } else {
            viewHolder.itemView.img_number_of_servings_recipe_row.alpha = 1f
            viewHolder.itemView.number_of_servings_recipe_row.alpha = 1f
        }

        if (post.cookingTime.isEmpty()) {
            viewHolder.itemView.img_time_recipe_row.alpha = 0.2f
            viewHolder.itemView.time_recipe_row.alpha = 0.2f
        } else {
            viewHolder.itemView.img_time_recipe_row.alpha = 1f
            viewHolder.itemView.time_recipe_row.alpha = 1f
        }

        if (post.ingredients.isEmpty()) {
            viewHolder.itemView.img_ingredients_text_view_recipe_row.alpha = 0.2f
            viewHolder.itemView.ingredients_text_view_recipe_row.alpha = 0.2f
        } else {
            viewHolder.itemView.img_ingredients_text_view_recipe_row.alpha = 1f
            viewHolder.itemView.ingredients_text_view_recipe_row.alpha = 1f
        }

        if (post.imageUrl.isEmpty()) viewHolder.itemView.image_recipe_row.setImageBitmap(defPostBitmap)

        else Picasso.get().load(post.imageUrl)
            .into(viewHolder.itemView.image_recipe_row)

       // Log.d(SEARCH_FRAGMENT_LOG, "VH: ${post.recipeName}, url=${post.imageUrl}, postUrl=${post.recipeUrl}")
    }
}