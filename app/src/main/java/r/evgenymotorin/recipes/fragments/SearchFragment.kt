package r.evgenymotorin.recipes.fragments

import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
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
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.parsing.Search
import android.support.v7.widget.RecyclerView
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.rows.RecipeRow


const val SEARCH_FRAGMENT_LOG = "search_fragment"

class SearchFragment : BaseFragment() {

    companion object {
        var isNowLoadingData = false
        var defPostBitmap : Bitmap? = null
    }

    private var adapter: GroupAdapter<ViewHolder> = GroupAdapter()

    private var lastSearch: String? = null

    private var currentPage = 1
    private var lastFirstItem = 0

    private var scrollDist = 0
    private var isSearchEditTextVisible = true
    private val minScrollDistance = 25

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.search_fragment, container, false)

        activity!!.title = getString(R.string.search)
        val editText = r.findViewById(R.id.edit_text_search_fragment) as EditText

        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).layoutManager = linearLayoutManager
        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).adapter = adapter

        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                inputMethodManager.hideSoftInputFromWindow(editText.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                if (lastSearch != editText.text.toString()) {
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                    lastSearch = editText.text.toString()
                } else return@setOnEditorActionListener false

                currentPage = 1
                lastFirstItem = 0
                Search().searchPostsInToRow(adapter, editText.text.toString(), currentPage++)
                return@setOnEditorActionListener true
            }
            false
        }

        return r
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPostBitmap = defaultPostBitmap
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
                }
                else if (!isSearchEditTextVisible && scrollDist < -minScrollDistance) {
                    showSearchView()
                    scrollDist = 0
                    isSearchEditTextVisible = true
                }
                if ((isSearchEditTextVisible && dy > 0) || (!isSearchEditTextVisible && dy < 0)) {
                    scrollDist += dy
                }

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !isNowLoadingData && firstVisibleItemPosition - lastFirstItem > 1) {
                    lastFirstItem = firstVisibleItemPosition
                    isNowLoadingData = true
                    Search().searchPostsInToRow(adapter, edit_text_search_fragment.text.toString(), currentPage++)
                    Log.d(SEARCH_FRAGMENT_LOG, "updating")
                }
            }
        })

        adapter.setOnItemClickListener { item, _ ->
            val row = (item as RecipeRow)

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivity(i)
        }
    }

    private fun hideSearchView() {
        edit_text_container_search_fragment.visibility = View.INVISIBLE
        inputMethodManager.hideSoftInputFromWindow(edit_text_search_fragment.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun showSearchView() {
        edit_text_container_search_fragment.visibility = View.VISIBLE
    }
}