package r.evgenymotorin.recipes.rows

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R

class LoadingRow(private val isBottomLoading: Boolean): Item<ViewHolder>() {
    override fun getLayout(): Int {
        return if (isBottomLoading) R.layout.bottom_loading_row
        else R.layout.center_loading_row

    }

    override fun bind(viewHolder: ViewHolder, position: Int) {}
}