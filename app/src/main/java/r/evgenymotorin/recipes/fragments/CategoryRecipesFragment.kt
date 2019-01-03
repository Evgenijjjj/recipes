package r.evgenymotorin.recipes.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.category_recipes_fragment.*
import r.evgenymotorin.recipes.MainActivity
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.parsing.Search
import r.evgenymotorin.recipes.rows.RecipeRow

const val CATEGORY_RECIPES_FR_LOG = "category_recipes"
class CategoryRecipesFragment: BaseFragment() {
    private var adapter: GroupAdapter<ViewHolder>? = null

    private var search: Search? = null
    private var categoryUrl: String? = null

    private var currentPage = 1
    private var lastFirstItem = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.category_recipes_fragment, container, false)

        v.findViewById<RecyclerView>(R.id.recycler_category_recipes_fragment).adapter = adapter
        v.findViewById<RecyclerView>(R.id.recycler_category_recipes_fragment).layoutManager = linearLayoutManager

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = GroupAdapter()
        search = Search()

        adapter!!.setOnItemClickListener { item, _ ->
            val row = (item as RecipeRow)

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivity(i)
        }

        categoryUrl = arguments?.getString(getString(R.string.category_url))
        if (categoryUrl.isNullOrEmpty()) Log.d(CATEGORY_RECIPES_FR_LOG, "url is null/empty")
    }

    override fun onStart() {
        super.onStart()

        searchPosts(currentPage++)

        recycler_category_recipes_fragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !search!!.isNowLoadingData() && firstVisibleItemPosition - lastFirstItem > 1) {

                    lastFirstItem = firstVisibleItemPosition
                    searchPosts(currentPage++)

                    Log.d(CATEGORY_RECIPES_FR_LOG, "updating")
                }
            }
        })
    }

    private fun searchPosts(pageNum: Int): Boolean {
        return if (!MainActivity.internetConnectionStatus) {
            Log.d(CATEGORY_RECIPES_FR_LOG, "bad internet connection")
            Toast.makeText(activity, activity?.getString(R.string.check_internet), Toast.LENGTH_LONG).show()
            currentPage--

            false
        } else {
            val url = "$categoryUrl&page=$pageNum"

            postsSearch.searchPostsInToRow(adapter!!, url, progress_bar_category_recipes_fragment)
            true
        }
    }
}