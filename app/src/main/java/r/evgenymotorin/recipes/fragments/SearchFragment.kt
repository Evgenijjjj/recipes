package r.evgenymotorin.recipes.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import r.evgenymotorin.recipes.R
import android.widget.EditText
import android.view.inputmethod.InputMethodManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.search_fragment.*
import r.evgenymotorin.recipes.di.base.BaseFragment
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import r.evgenymotorin.recipes.MainActivity.Companion.internetConnectionStatus
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.parsing.Search
import r.evgenymotorin.recipes.rows.RecipeRow
import java.net.URLEncoder


const val SEARCH_FRAGMENT_LOG = "search_fragment"

class SearchFragment : BaseFragment() {

    companion object {
        var defPostBitmap: Bitmap? = null
    }

    private var adapter: GroupAdapter<ViewHolder> = GroupAdapter()
    private var search: Search? = null

    private var lastSearch: String? = null

    private var currentPage = 1
    private var lastFirstItem = 0

    private var scrollDist = 0
    private var isSearchEditTextVisible = true
    private val minScrollDistance = 25

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.search_fragment, container, false)

        val editText = r.findViewById<EditText>(R.id.edit_text_search_fragment)

        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).adapter = adapter
        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).layoutManager = linearLayoutManager

        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(
                    editText.applicationWindowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )

                if (lastSearch != editText.text.toString()) {
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                    lastSearch = editText.text.toString()
                } else return@setOnEditorActionListener false

                currentPage = 1
                lastFirstItem = 0
                searchPosts(currentPage++)

                return@setOnEditorActionListener true
            }
            false
        }

        r.findViewById<CardView>(R.id.update_btn_search_fragment).setOnClickListener {
            this.searchPosts(currentPage)
        }

        return r
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPostBitmap = defaultPostBitmap
        search = Search(dbHelper)

        adapter.setOnItemClickListener { item, _ ->
            val row = (item as RecipeRow)

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivity(i)
        }
    }

    override fun onStart() {
        super.onStart()

        recycler_view_search_fragment.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (isSearchEditTextVisible && scrollDist > minScrollDistance) {
                    hideSearchView()
                    scrollDist = 0
                    isSearchEditTextVisible = false
                } else if (!isSearchEditTextVisible && scrollDist < -minScrollDistance) {
                    showSearchView()
                    scrollDist = 0
                    isSearchEditTextVisible = true
                }
                if ((isSearchEditTextVisible && dy > 0) || (!isSearchEditTextVisible && dy < 0)) {
                    scrollDist += dy
                }

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !search!!.isNowLoadingData() && firstVisibleItemPosition - lastFirstItem > 1) {

                    lastFirstItem = firstVisibleItemPosition
                    searchPosts(currentPage++)

                    Log.d(SEARCH_FRAGMENT_LOG, "updating")
                }
            }
        })
    }

    private fun hideSearchView() {
        edit_text_container_search_fragment.visibility = View.INVISIBLE
        inputMethodManager.hideSoftInputFromWindow(
            edit_text_search_fragment.applicationWindowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showSearchView() {
        edit_text_container_search_fragment.visibility = View.VISIBLE
    }

    private fun searchPosts(pageNum: Int): Boolean {
        return if (!internetConnectionStatus) {
            Log.d(SEARCH_FRAGMENT_LOG, "bad internet connection")
            Toast.makeText(activity, activity?.getString(R.string.check_internet), Toast.LENGTH_LONG).show()
            currentPage--
            lastSearch = null

            update_btn_search_fragment.visibility = View.VISIBLE
            false
        } else {
            update_btn_search_fragment.visibility = View.INVISIBLE

            val url = getString(R.string.base_site_url) +
                    "/retsepty?page=$pageNum&query=${URLEncoder.encode(
                        edit_text_search_fragment.text.toString(),
                        "utf-8"
                    )}"

            postsSearch.searchPostsInToRow(adapter, url, progress_bar_search_fragment, first_load_progress_search_fragment)
            true
        }
    }
}