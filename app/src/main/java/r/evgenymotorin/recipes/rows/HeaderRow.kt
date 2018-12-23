package r.evgenymotorin.recipes.rows

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.header_row.view.*
import r.evgenymotorin.recipes.R

class HeaderRow(private val header: String): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.header_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.text_header_row.text = header
   }
}