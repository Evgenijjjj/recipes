package r.evgenymotorin.recipes.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
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
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.Toast
import r.evgenymotorin.recipes.MainActivity.Companion.internetConnectionStatus
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.model.ClickedRecipe
import r.evgenymotorin.recipes.parsing.Search
import r.evgenymotorin.recipes.rows.RecipeRow
import java.net.URLEncoder
import r.evgenymotorin.recipes.rows.SearchHistoryRow
import r.evgenymotorin.recipes.database.tables.SearchHistoryData


const val SEARCH_FRAGMENT_LOG = "search_fragment"

class SearchFragment : BaseFragment(), TextWatcher {

    companion object {
        var defPostBitmap: Bitmap? = null
    }

    private lateinit var adapter: GroupAdapter<ViewHolder>
    private var search: Search? = null

    private var lastSearch: String? = null

    private var currentPage = 1
    private var lastFirstItem = 0

    private var scrollDist = 0
    private var isSearchEditTextVisible = true
    private val minScrollDistance = 25

    private val clickedRecipe = ClickedRecipe()
    private lateinit var searchHistoryAdapter: GroupAdapter<ViewHolder>
    private lateinit var searchHistoryList: List<SearchHistoryData>

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.search_fragment, container, false)

        val editText = r.findViewById<EditText>(R.id.edit_text_search_fragment)

        r.findViewById<ImageView>(R.id.clear_search_fragment).setOnClickListener { editText.setText(""); it.visibility = View.INVISIBLE }
        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).adapter = adapter
        r.findViewById<RecyclerView>(R.id.recycler_view_search_fragment).layoutManager = linearLayoutManager

        r.findViewById<RecyclerView>(R.id.search_history_recyclerView).adapter = searchHistoryAdapter
        val historyRoot = r.findViewById<CardView>(R.id.search_history_root)

        editText.addTextChangedListener(this)

        editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchHistoryAdapter.clear()
                if (searchHistoryList.isEmpty()) return@setOnFocusChangeListener
                historyRoot.visibility = View.VISIBLE

                for (data in searchHistoryList)
                    searchHistoryAdapter.add(SearchHistoryRow(data))
            }
        }


        editText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                v.clearFocus()
                historyRoot.visibility = View.INVISIBLE
                inputMethodManager.hideSoftInputFromWindow(editText.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)

                val request = editText.text.toString()

                if (lastSearch != request) {
                    adapter.clear()
                    adapter.notifyDataSetChanged()
                    lastSearch = request
                } else return@setOnEditorActionListener false

                currentPage = 1
                lastFirstItem = 0
                searchPosts(currentPage++)


                if (request.isNotEmpty() && db.SearchHistoryDataDao().getDataWithRequest(request) == null) {
                    val shd = SearchHistoryData()
                    shd.request = request
                    db.SearchHistoryDataDao().insert(shd)
                }

                searchHistoryList = db.SearchHistoryDataDao().getAll().reversed()
                return@setOnEditorActionListener true
            }
            false
        }

        r.findViewById<FloatingActionButton>(R.id.update_btn_search_fragment).setOnClickListener {
            this.searchPosts(currentPage)
        }

        return r
    }

    override fun afterTextChanged(s: Editable?) {}

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        searchHistoryAdapter.clear()

        if (!s.isNullOrEmpty()) {
            clear_search_fragment.visibility = View.VISIBLE
            for (w in searchHistoryList)
                if (w.request.toLowerCase().contains(s.toString().toLowerCase()))
                    searchHistoryAdapter.add(SearchHistoryRow(w))
        } else if (edit_text_search_fragment.isFocused){
            clear_search_fragment.visibility = View.VISIBLE
            for (w in searchHistoryList)
                searchHistoryAdapter.add(SearchHistoryRow(w))
        }

        search_history_root.visibility = if (searchHistoryAdapter.itemCount == 0) View.INVISIBLE else View.VISIBLE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GroupAdapter()
        searchHistoryAdapter = GroupAdapter()
        defPostBitmap = defaultPostBitmap
        search = Search(dbHelper)

        searchHistoryList = db.SearchHistoryDataDao().getAll().reversed()

        adapter.setOnItemClickListener { item, view ->
            val row: RecipeRow

            try {
                row = (item as RecipeRow)
            } catch (e: ClassCastException) {
                return@setOnItemClickListener
            }

            clickedRecipe.row = row
            clickedRecipe.view = view
            clickedRecipe.position = recycler_view_search_fragment.getChildAdapterPosition(view)
            clickedRecipe.group = adapter.getItem(clickedRecipe.position!!)

            val i = Intent(activity, RecipeActivity::class.java)
            i.putExtra(getString(R.string.recipeName), row.recipeName)
            i.putExtra(getString(R.string.recipeLink), row.recipeLink)
            startActivityForResult(i, activity!!.resources.getInteger(R.integer.recipesRequestCode))
        }

        searchHistoryAdapter.setOnItemClickListener { item, _ ->
            val row = item as SearchHistoryRow
            edit_text_search_fragment.setText(row.request.request)
            edit_text_search_fragment.setSelection(edit_text_search_fragment.text.length)
            search_history_root.visibility = View.INVISIBLE
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try { if (resultCode == Activity.RESULT_OK && requestCode == activity!!.resources.getInteger(R.integer.recipesRequestCode) && data != null) {
                adapter.remove(clickedRecipe.group)
                adapter.add(clickedRecipe.position!!, RecipeRow(clickedRecipe.row.post, data.getBooleanExtra(getString(R.string.recipesString), true)))
            } } catch (e: Exception) {
            edit_text_search_fragment.clearFocus()
            inputMethodManager.hideSoftInputFromWindow(edit_text_search_fragment.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
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

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !postsSearch.isNowLoadingData() && firstVisibleItemPosition - lastFirstItem > 1) {

                    lastFirstItem = firstVisibleItemPosition
                    searchPosts(currentPage++)

                    Log.d(SEARCH_FRAGMENT_LOG, "updating")
                }
            }
        })
    }

    private fun hideSearchView() {
        edit_text_container_search_fragment.visibility = View.INVISIBLE
        search_history_root.visibility = View.INVISIBLE
        inputMethodManager.hideSoftInputFromWindow(
            edit_text_search_fragment.applicationWindowToken,
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private fun showSearchView() {
        edit_text_container_search_fragment.visibility = View.VISIBLE
    }

    @SuppressLint("RestrictedApi")
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
                    "/retsepty?page=$pageNum&query=${URLEncoder.encode(edit_text_search_fragment.text.toString()
                        , "utf-8")}"

            postsSearch.searchPostsInToRow(adapter, url)
            true
        }
    }

    override fun onPause() {
        super.onPause()
        search_history_root.visibility = View.INVISIBLE
        inputMethodManager.hideSoftInputFromWindow(edit_text_search_fragment.applicationWindowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}