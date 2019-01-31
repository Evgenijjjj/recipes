package r.evgenymotorin.recipes.rows

import android.view.View
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.step_row.view.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.tables.StepData
import r.evgenymotorin.recipes.model.Step

class StepRow(private val indexNum: String, private val step: StepData): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.step_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.index_number_step_row.text = indexNum
        viewHolder.itemView.description_step_row.text = step.stepDescription

        if (step.stepImageUrl != null) {
            viewHolder.itemView.image_container_step_row.visibility = View.VISIBLE
            Picasso.get().load(step.stepImageUrl).into(viewHolder.itemView.image_step_row)
        } else
            viewHolder.itemView.image_container_step_row.visibility = View.GONE
    }
}