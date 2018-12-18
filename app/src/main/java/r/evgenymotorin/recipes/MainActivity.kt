package r.evgenymotorin.recipes

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import r.evgenymotorin.recipes.fragments.SearchFragment

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.menu.getItem(0).isChecked = true
        nav_view.setNavigationItemSelectedListener(this)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_box_content_main, SearchFragment(), getString(R.string.searchFragment))
            .addToBackStack(getString(R.string.searchFragment))
            .commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_settings -> return true
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_search -> run {
                if (fragmentIsExists(getString(R.string.searchFragment))) return@run
                supportFragmentManager.beginTransaction()
                    .add(R.id.main_box_content_main, SearchFragment(), getString(R.string.searchFragment))
                    .addToBackStack(getString(R.string.searchFragment))
                    .commit()
            }
            R.id.nav_categories -> {

            }
            R.id.nav_favorites -> {

            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

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
}
