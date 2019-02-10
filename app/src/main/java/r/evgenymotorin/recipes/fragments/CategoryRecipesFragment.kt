package r.evgenymotorin.recipes.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.category_recipes_fragment.*
import r.evgenymotorin.recipes.MainActivity
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.di.base.BaseFragment
import r.evgenymotorin.recipes.model.ClickedRecipe
import r.evgenymotorin.recipes.parsing.Search
import r.evgenymotorin.recipes.rows.RecipeRow

const val CATEGORY_RECIPES_FR_LOG = "category_recipes"

class CategoryRecipesFragment : BaseFragment() {
    private lateinit var adapter: GroupAdapter<ViewHolder>

    private var search: Search? = null
    private var categoryUrl: String? = null

    private var currentPage = 1
    private var lastFirstItem = 0

    private val clickedRecipe = ClickedRecipe()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.category_recipes_fragment, container, false)

        v.findViewById<RecyclerView>(R.id.recycler_category_recipes_fragment).adapter = adapter
        v.findViewById<RecyclerView>(R.id.recycler_category_recipes_fragment).layoutManager = linearLayoutManager

        v.findViewById<FloatingActionButton>(R.id.update_btn_category_recipes_fragment).setOnClickListener {
            this.searchPosts(currentPage)
        }
        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        adapter = GroupAdapter()
        search = Search(dbHelper)

        adapter.setOnItemClickListener { item, view ->
            var row: RecipeRow

            try {
                row = (item as RecipeRow)
            } catch (e: ClassCastException) {
                return@setOnItemClickListener
            }

            clickedRecipe.row = row
            clickedRecipe.view = view
            clickedRecipe.position = recycler_category_recipes_fragment.getChildAdapterPosition(view)
            clickedRecipe.group = adapter.getItem(clickedRecipe.position!!)

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivityForResult(i, activity!!.resources.getInteger(R.integer.recipesRequestCode))
        }

        categoryUrl = arguments?.getString(getString(R.string.category_url))
        if (categoryUrl.isNullOrEmpty()) Log.d(CATEGORY_RECIPES_FR_LOG, "url is null/empty")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try { if (resultCode == Activity.RESULT_OK && requestCode == activity!!.resources.getInteger(R.integer.recipesRequestCode) && data != null) {
                adapter.remove(clickedRecipe.group)
                adapter.add(clickedRecipe.position!!, RecipeRow(clickedRecipe.row.post, data.getBooleanExtra(getString(R.string.recipesString), true)))
            } } catch (e: Exception) { }
    }

    override fun onStart() {
        super.onStart()

        if (recycler_category_recipes_fragment.childCount == 0) searchPosts(currentPage++)


        recycler_category_recipes_fragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !postsSearch.isNowLoadingData() && firstVisibleItemPosition - lastFirstItem > 1) {

                    lastFirstItem = firstVisibleItemPosition
                    searchPosts(currentPage++)

                    Log.d(CATEGORY_RECIPES_FR_LOG, "updating")
                }
            }
        })
    }

    @SuppressLint("RestrictedApi")
    private fun searchPosts(pageNum: Int): Boolean {
        return if (!MainActivity.internetConnectionStatus) {
            Log.d(CATEGORY_RECIPES_FR_LOG, "bad internet connection")
            Toast.makeText(activity, activity?.getString(R.string.check_internet), Toast.LENGTH_LONG).show()
            currentPage--

            update_btn_category_recipes_fragment.visibility = View.VISIBLE
            false
        } else {
            val url = "$categoryUrl&page=$pageNum"
            update_btn_category_recipes_fragment.visibility = View.INVISIBLE

            postsSearch.searchPostsInToRow(adapter, url)
            true
        }
    }
}