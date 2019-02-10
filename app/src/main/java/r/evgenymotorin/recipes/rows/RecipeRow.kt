package r.evgenymotorin.recipes.rows

import android.os.Build
import android.support.annotation.RequiresApi
import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.recipe_row.view.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.db.tables.RecipeData
import r.evgenymotorin.recipes.fragments.SearchFragment.Companion.defPostBitmap

class RecipeRow(val post: RecipeData, private val isFavourite: Boolean): Item<ViewHolder>() {
    var recipeLink = ""
    var recipeName = ""

    override fun getLayout(): Int {
        return R.layout.recipe_row
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.name_recipe_row.text = post.recipeName

        recipeLink = post.recipeUrl
        recipeName = post.recipeName

        if (isFavourite) viewHolder.itemView.favourite_status_recipe_row.visibility = View.VISIBLE
        else viewHolder.itemView.favourite_status_recipe_row.visibility = View.INVISIBLE


        viewHolder.itemView.number_of_servings_recipe_row.text = post.numberOfServings
        viewHolder.itemView.time_recipe_row.text = post.cookingTime
        viewHolder.itemView.ingredients_text_view_recipe_row.text = post.ingredientsCount

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

        if (post.ingredientsCount.isEmpty() || post.ingredientsCount == "0") {
            viewHolder.itemView.img_ingredients_text_view_recipe_row.alpha = 0.2f
            viewHolder.itemView.ingredients_text_view_recipe_row.visibility = View.GONE
        } else {
            viewHolder.itemView.img_ingredients_text_view_recipe_row.alpha = 1f
            viewHolder.itemView.ingredients_text_view_recipe_row.visibility = View.VISIBLE
        }

        if (post.imageUrl.isNullOrEmpty()) viewHolder.itemView.image_recipe_row.setImageBitmap(defPostBitmap)

        else Picasso.get().load(post.imageUrl)
            .into(viewHolder.itemView.image_recipe_row)
    }
}