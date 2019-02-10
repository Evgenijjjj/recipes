package r.evgenymotorin.recipes.rows

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R

class NothingFoundRow: Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.nothing_found_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {}
}