package r.evgenymotorin.recipes

import android.support.design.widget.TabLayout


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recipe.*
import org.jsoup.nodes.Document
import r.evgenymotorin.recipes.di.activity.BaseActivity
import r.evgenymotorin.recipes.fragments.AboutFragment
import r.evgenymotorin.recipes.fragments.IngredientsFragment
import r.evgenymotorin.recipes.fragments.StepsFragment
import r.evgenymotorin.recipes.parsing.Parsers
import r.evgenymotorin.recipes.parsing.Query

class RecipeActivity : BaseActivity() {

    companion object {
        var pageHTML: Document? = null
    }

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(getString(R.string.recipeName))

        if (intent.getStringExtra(getString(R.string.recipeLink)) != null) {
            Query().downloadPage(intent.getStringExtra(getString(R.string.recipeLink))).subscribe {
                pageHTML = it
                initLogic()
            }

        } else {
            Toast.makeText(this, "intent error", Toast.LENGTH_LONG).show()
            return
        }
    }

    private fun initLogic() {
        var tabsCount = 3

        /** Remove about fragment from adapter if about information is empty */
        if (pageHTML != null && Parsers().scrapAboutInformationFromHTML(pageHTML!!) == null) {
            tabs.removeTabAt(0)
            tabsCount = 2
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, tabsCount)
        recipe_container.adapter = mSectionsPagerAdapter
        recipe_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(recipe_container))
        tabs.visibility = View.VISIBLE
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private inner class SectionsPagerAdapter(fm: FragmentManager,private val pagesCount: Int) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            return when (position) {
                pagesCount - 3 -> {
                    fragmentIsExists(getString(R.string.aboutFragment)) ?: AboutFragment()
                }
                pagesCount - 2 -> {
                    fragmentIsExists(getString(R.string.ingredientsFragment)) ?: IngredientsFragment()
                }

                pagesCount - 1-> {
                    fragmentIsExists(getString(R.string.stepsFragment)) ?: StepsFragment()
                }

                else -> Fragment()
            }
        }

        override fun getCount(): Int {
            return pagesCount
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fragmentIsExists(fragmentTag: String): Fragment? {
        return supportFragmentManager.findFragmentByTag(fragmentTag) ?: return null
    }

}