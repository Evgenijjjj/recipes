package r.evgenymotorin.recipes.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.di.base.BaseFragment
import r.evgenymotorin.recipes.rows.RecipeRow

private const val FAVOURITES_FRAGMENT_LOG = "favourites_fragment_log"
class FavouritesFragment: BaseFragment() {
    private lateinit var adapter: GroupAdapter<ViewHolder>


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.favourites_fragment, container, false)
        v.findViewById<RecyclerView>(R.id.favourites_recyclerview).adapter = adapter
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupAdapter()

        adapter.setOnItemClickListener { item, _ ->
            val row = item as RecipeRow

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivity(i)
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.clear()

        for(fd in  db.FavouritesDataDao().getAll())
            adapter.add(RecipeRow(db.RecipeDataDao().getRecipeWithUrl(fd.recipeUrl)!!))

        if (adapter.itemCount == 0)
            view?.findViewById<TextView>(R.id.empty_list_textview_favourites_fragment)?.visibility = View.VISIBLE
        else
            view?.findViewById<TextView>(R.id.empty_list_textview_favourites_fragment)?.visibility = View.INVISIBLE
    }
}