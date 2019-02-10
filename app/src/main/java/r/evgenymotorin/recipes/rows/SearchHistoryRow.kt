package r.evgenymotorin.recipes.rows

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.search_history_row.view.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.tables.SearchHistoryData

class SearchHistoryRow(val request: SearchHistoryData): Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.search_history_row
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.request_tv_search_history_row.text = request.request
    }
}