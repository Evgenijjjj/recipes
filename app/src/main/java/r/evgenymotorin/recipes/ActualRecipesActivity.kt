package r.evgenymotorin.recipes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.actual_recipes_activity.*
import kotlinx.android.synthetic.main.category_recipes_fragment.*
import r.evgenymotorin.recipes.di.base.BaseActivity
import r.evgenymotorin.recipes.model.ClickedRecipe
import r.evgenymotorin.recipes.rows.RecipeRow
import r.evgenymotorin.recipes.settings.ActualPreferenceFragment


class ActualRecipesActivity : BaseActivity(), OnItemClickListener {
    private lateinit var adapter: GroupAdapter<ViewHolder>
    private lateinit var collapsedMenu: Menu
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var appBarExpanded = true

    private lateinit var currentTimePeriod: String
    private lateinit var currentParams: String
    private var currentPage = 1
    private lateinit var url: String
    private var lastFirstItem = 0

    private val clickedRecipe = ClickedRecipe()

    private var isPreferenceModeEnabled = false
    private lateinit var actualPreferenceFragment: ActualPreferenceFragment


    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.actual_recipes_activity)

        setSupportActionBar(findViewById(R.id.anim_toolbar_actual))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        collapsing_toolbar_actual.title = getCurrentPeriod()

        collapsing_toolbar_actual.setContentScrimColor(getColor(R.color.colorPrimary))
        collapsing_toolbar_actual.setStatusBarScrimColor(getColor(R.color.colorPrimary))

        adapter = GroupAdapter()
        adapter.setOnItemClickListener(this)
        actual_recyclerView.adapter = adapter

        linearLayoutManager = LinearLayoutManager(this)
        actual_recyclerView.layoutManager = linearLayoutManager

        currentTimePeriod = getCurrentPeriod()
        currentParams = defaultSharedPreferences.getString(getString(R.string.filter_params_key),
            getString(R.string.default_value_params))!!

        actualPreferenceFragment = ActualPreferenceFragment()
        actualPreferenceFragment.callback = {this.onResume()}

        actual_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                if (totalItemCount - visibleItemCount - firstVisibleItemPosition <= 0 && !postsSearch.isNowLoadingData() && firstVisibleItemPosition - lastFirstItem > 1) {

                    lastFirstItem = firstVisibleItemPosition
                    searchPosts(url)
                    currentPage++
                }
            }
        })

        appbar_actual.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            //  Vertical offset == 0 indicates appBar is fully expanded.
            if (Math.abs(verticalOffset) > 200) {
                appBarExpanded = false
                invalidateOptionsMenu()
            } else {
                appBarExpanded = true
                invalidateOptionsMenu()
            }
        })


        floating_btn_actual_activity.setOnClickListener {
            fragmentManager.beginTransaction()
                .replace(R.id.prefs_fragment_box_actual_activity, actualPreferenceFragment)
                .commit()

            enablePreferenceMode(false)
        }

        floating_btn_actual_refresh.setOnClickListener {
            it.visibility = View.INVISIBLE
            searchPosts(url)
        }
    }

    /**
     * ADAPTER ITEM CLICK LISTENER
     */
    override fun onItemClick(item: Item<*>, view: View) {
        var row: RecipeRow

        try {
            row = (item as RecipeRow)
        } catch (e: ClassCastException) {
            return
        }

        clickedRecipe.row = row
        clickedRecipe.view = view
        clickedRecipe.position = actual_recyclerView.getChildAdapterPosition(view)
        clickedRecipe.group = adapter.getItem(clickedRecipe.position!!)

        val i = Intent(this, RecipeActivity::class.java)
        i.putExtra(getString(R.string.recipeName), row.recipeName)
        i.putExtra(getString(R.string.recipeLink), row.recipeLink)
        startActivityForResult(i, resources.getInteger(R.integer.recipesRequestCode))
    }

    /**
     * CHECK FOR ADDING/REMOVING RECIPE TO DB
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        try { if (resultCode == Activity.RESULT_OK && requestCode == resources.getInteger(R.integer.recipesRequestCode) && data != null) {
                adapter.remove(clickedRecipe.group)
                adapter.add(clickedRecipe.position!!, RecipeRow(clickedRecipe.row.post, data.getBooleanExtra(getString(R.string.recipesString), true)))
            } } catch (e: Exception) { }
    }

    /**
     * CHECK FOR CHANGES IN SEARCH FILTER
     */
    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)

        isPreferenceModeEnabled = false
        enablePreferenceMode(true)

        floating_btn_actual_activity.isEnabled = true

        val period = getCurrentPeriod()
        val params = defaultSharedPreferences.getString(getString(R.string.filter_params_key), getString(R.string.default_value_params))!!

        if (collapsing_toolbar_actual.title != period || currentParams != params || adapter.itemCount == 0) {
            lastFirstItem = 0

            collapsing_toolbar_actual.title = period
            currentParams = params
            currentTimePeriod = period

            adapter.clear()
            adapter.notifyDataSetChanged()
            currentPage = 1

            url = getString(R.string.base_site_url) + "/retsepty?tags%5Brecipe_hidden_tag%5D%5B%5D=" +
                    defaultSharedPreferences.getString(getString(R.string.filter_period_key), getString(R.string.default_value_period)) +
                    params

            searchPosts(url)
            currentPage++
        }
    }

    /**
     * ENABLED WHILE FILTER SETTINGS FRAGMENT OPEN
     */
    private fun enablePreferenceMode(isNotEnabled: Boolean) {
        floating_btn_actual_activity.isEnabled = isNotEnabled
        supportActionBar?.setDisplayHomeAsUpEnabled(isNotEnabled)

        if (isNotEnabled) {
            adapter.setOnItemClickListener(this)
        } else {
            adapter.setOnItemClickListener(null)
        }

        adapter.notifyDataSetChanged()
        isPreferenceModeEnabled = false
    }

    /**
     * OPTIONS MENU PART
     */
    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        if (!appBarExpanded || collapsedMenu.size() != 1) {
            collapsedMenu.add("Add")
                .setIcon(R.drawable.settings)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        } else {}
        return super.onPrepareOptionsMenu(collapsedMenu)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_actual, menu)
        collapsedMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (isPreferenceModeEnabled) return false
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }

        }
        if (item.title === "Add") {
            fragmentManager.beginTransaction()
                .replace(R.id.prefs_fragment_box_actual_activity, actualPreferenceFragment)
                .commit()

            enablePreferenceMode(false)
        }

        return super.onOptionsItemSelected(item)
    }

    /**
     * FIND NEW POSTS IN RECYCLER
     */
    @SuppressLint("RestrictedApi")
    private fun searchPosts(url: String): Boolean {
        return if (!MainActivity.internetConnectionStatus) {
            Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_LONG).show()
            currentPage--
            floating_btn_actual_refresh.visibility = View.VISIBLE
            false
        } else {
            floating_btn_actual_refresh.visibility = View.INVISIBLE
            postsSearch.searchSpecialPostsInToRow(adapter, "$url&page=$currentPage")
            true
        }
    }
}
