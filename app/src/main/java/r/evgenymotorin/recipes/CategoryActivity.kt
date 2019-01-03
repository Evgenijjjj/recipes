package r.evgenymotorin.recipes

import android.support.design.widget.TabLayout

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import kotlinx.android.synthetic.main.activity_category.*
import kotlinx.android.synthetic.main.fragment_category.view.*
import r.evgenymotorin.recipes.di.activity.BaseActivity
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.fragments.*


const val CATEGORY_ACTIVITY_LOG = "category_activity"

class CategoryActivity : BaseActivity() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var categoryName: String? = null
    private var categoryUrl: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        setSupportActionBar(toolbar)
        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        categoryName = intent.getStringExtra(getString(R.string.category_name))

        if (categoryName.isNullOrEmpty()) {
            Log.d(CATEGORY_ACTIVITY_LOG, "intent error")
            return
        } else if (!findUrlForCategory(categoryName!!)) {
            Log.d(CATEGORY_ACTIVITY_LOG, "category_url error")
            return
        }

        container.adapter = mSectionsPagerAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_category, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }


    /**
     * A [FragmentPagerAdapter] that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    private inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val fr = CategoryRecipesFragment()
            val b = Bundle()

            Log.d(CATEGORY_ACTIVITY_LOG, "position: $position")

            when (position) {
                0 -> b.putString(getString(R.string.category_url), "$categoryUrl&field=published_at")
                1 -> b.putString(getString(R.string.category_url), "$categoryUrl&field=popular")
                else -> return Fragment()
            }

            fr.arguments = b
            return  fr
        }

        override fun getCount(): Int {
            return 2
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    class PlaceholderFragment : BaseFragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val rootView = inflater.inflate(R.layout.fragment_category, container, false)
            rootView.section_label.text = getString(R.string.section_format, arguments?.getInt(ARG_SECTION_NUMBER))
            return rootView
        }

        companion object {
            /**
             * The fragment argument representing the section number for this
             * fragment.
             */
            private val ARG_SECTION_NUMBER = "section_number"

            /**
             * Returns a new instance of this fragment for the given section
             * number.
             */
            fun newInstance(sectionNumber: Int): CategoryRecipesFragment {
                val fragment = CategoryRecipesFragment()
                val args = Bundle()
                args.putInt(ARG_SECTION_NUMBER, sectionNumber)
                fragment.arguments = args
                return fragment
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun findUrlForCategory(category: String): Boolean {
        categoryUrl = getString(R.string.base_site_url) + "/retsepty?"

        Log.d(CATEGORY_ACTIVITY_LOG, "categoryName = $category")
        categoryUrl += when (category) {
            getString(R.string.dessert_category) -> {
                supportActionBar?.title = getString(R.string.dessertsString)
                getString(R.string.dessert_category_url)
            }
            getString(R.string.main_food_category) -> {
                supportActionBar?.title = getString(R.string.mainFoodString)
                getString(R.string.main_food_category_url)
            }
            getString(R.string.meat_category) -> {
                supportActionBar?.title = getString(R.string.meatString)
                getString(R.string.meat_category_url)
            }
            getString(R.string.salad_category) -> {
                supportActionBar?.title = getString(R.string.saladsString)
                getString(R.string.salad_category_url)
            }
            getString(R.string.soup_category) -> {
                supportActionBar?.title = getString(R.string.soupsString)
                getString(R.string.soup_category_url)
            }
            getString(R.string.bakery_category) -> {
                supportActionBar?.title = getString(R.string.bakeryString)
                getString(R.string.bakery_category_url)
            }
            getString(R.string.snacks_category) -> {
                supportActionBar?.title = getString(R.string.snacksString)
                getString(R.string.snacks_category_url)
            }
            getString(R.string.drinks_category) -> {
                supportActionBar?.title = getString(R.string.drinksString)
                getString(R.string.drinks_category_url)
            }
            else -> {
                return false
            }
        }
        Log.d(CATEGORY_ACTIVITY_LOG, "url: $categoryUrl")
        return true
    }
}
