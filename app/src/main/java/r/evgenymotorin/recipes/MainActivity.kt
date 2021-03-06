package r.evgenymotorin.recipes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.view.*
import r.evgenymotorin.recipes.di.base.BaseActivity
import r.evgenymotorin.recipes.fragments.CategoriesFragment
import r.evgenymotorin.recipes.fragments.FavouritesFragment
import r.evgenymotorin.recipes.fragments.SearchFragment

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var internetConnectionStatus: Boolean = false
    }

    private var checkConnectionAsync: CheckConnectionAsync? = null
    private var bottomMenu: Menu? = null
    private lateinit var headerView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        checkConnectionAsync = CheckConnectionAsync()
        checkConnectionAsync?.execute()

        val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.menu.getItem(0).isChecked = true
        nav_view.setNavigationItemSelectedListener(this)

        headerView = View.inflate(this, R.layout.nav_header_main, null)
        nav_view.addHeaderView(headerView)

        headerView.setOnClickListener {
            it.isEnabled = false
            startActivity(actualRecipesActivityIntent)
            Handler().postDelayed({
                drawer_layout.closeDrawer(GravityCompat.START)
            }, 500)
        }

        title = getString(R.string.categories)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_box_content_main, CategoriesFragment(), getString(R.string.categoriesFragment))
            .addToBackStack(getString(R.string.categoriesFragment))
            .commit()
    }

    override fun onResume() {
        super.onResume()
        overridePendingTransition(0, 0)

        headerView.isEnabled = true
        headerView.recipe_period_textView.text = getCurrentPeriod()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        bottomMenu = menu
        bottomMenu!!.getItem(0).title = getCurrentPeriod()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.action_open_actual_recipes_activity) {
            startActivity(actualRecipesActivityIntent)
            true
        } else false
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search -> run {
                title = getString(R.string.search)

                if (fragmentIsExists(getString(R.string.searchFragment))) return@run
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_box_content_main, SearchFragment(), getString(R.string.searchFragment))
                    .addToBackStack(getString(R.string.searchFragment))
                    .commit()
            }
            R.id.nav_categories -> run {
                title = getString(R.string.categories)
                if (fragmentIsExists(getString(R.string.categoriesFragment))) return@run
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_box_content_main, CategoriesFragment(), getString(R.string.categoriesFragment))
                    .addToBackStack(getString(R.string.categoriesFragment))
                    .commit()
            }
            R.id.nav_favorites -> run {
                title = getString(R.string.favourites)

                if (fragmentIsExists(getString(R.string.favouritesFragment))) return@run
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_box_content_main, FavouritesFragment(), getString(R.string.favouritesFragment))
                    .addToBackStack(getString(R.string.favouritesFragment))
                    .commit()
            }
            R.id.nav_share -> {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$packageName")))
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun fragmentIsExists(fragmentTag: String): Boolean {
        val fragment = supportFragmentManager.findFragmentByTag(fragmentTag) ?: return false

        for (f in supportFragmentManager.fragments){
            supportFragmentManager.beginTransaction().hide(f).commit()
        }
        supportFragmentManager.beginTransaction().show(fragment).commit()
        return true
    }

    @SuppressLint("StaticFieldLeak")
    private inner class CheckConnectionAsync: AsyncTask<Void, Void, Void>() {
        private var executeFlag = true

        override fun doInBackground(vararg params: Void?): Void? {
            while (executeFlag) {
                Thread.sleep(500)
                internetConnection.subscribe { status -> internetConnectionStatus = status }
            }
            return null
        }

        fun setExecuteFlag(b: Boolean) {
            this.executeFlag = b
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        checkConnectionAsync?.setExecuteFlag(false)
    }
}
