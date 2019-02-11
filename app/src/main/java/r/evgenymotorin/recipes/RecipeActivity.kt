package r.evgenymotorin.recipes

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.support.design.widget.TabLayout


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_recipe.*
import org.jsoup.nodes.Document
import r.evgenymotorin.recipes.database.tables.FavouritesData
import r.evgenymotorin.recipes.db.tables.RecipeData
import r.evgenymotorin.recipes.di.base.BaseActivity
import r.evgenymotorin.recipes.fragments.AboutFragment
import r.evgenymotorin.recipes.fragments.IngredientsFragment
import r.evgenymotorin.recipes.fragments.StepsFragment
import r.evgenymotorin.recipes.parsing.Parsers
import r.evgenymotorin.recipes.parsing.Query

const val RECIPE_ACTIVITY_LOG = "recipe_activity"
class RecipeActivity : BaseActivity() {

    private var recipeUrl: String? = null
    private var recipeDataId = -1
    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null
    private var topMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(getString(R.string.recipeName))

        recipeUrl = intent.getStringExtra(getString(R.string.recipeLink))
        var recipe = db.RecipeDataDao().getRecipeWithUrl(recipeUrl!!)

        if (recipeUrl != null && recipe != null) {
            recipeDataId = recipe.id!!

            /**
             * checking for additional information in the database and internet connection
             */
            val isNotFullRecipeInDB = recipe.adaptiveIngredientFlag == null && recipe.firstStepPtr == null

            if (isNotFullRecipeInDB && MainActivity.internetConnectionStatus)
            Query().scrapDetailedInfoForRecipe(dbHelper, recipe)
                .subscribe {
                    recipe = db.RecipeDataDao().getRecipeWithUrl(recipeUrl!!)
                    initLogic(recipe!!)
                }
            else if (!isNotFullRecipeInDB) initLogic(recipe!!)
            else {
                Toast.makeText(this, getString(R.string.check_internet), Toast.LENGTH_LONG).show()
                finish(); return
            }

        } else {
            Toast.makeText(this, "intent/db error", Toast.LENGTH_LONG).show()
            return
        }
    }

    private fun initLogic(recipe: RecipeData) {
        var tabsCount = 3

        /** Remove about fragment from adapter if about information is empty */
        if (recipe.aboutDescription.isNullOrEmpty() && recipe.firstImgAboutPtr == null) {
            tabs.removeTabAt(0)
            tabsCount = 2
        }

        mSectionsPagerAdapter = SectionsPagerAdapter(supportFragmentManager, tabsCount)
        recipe_container.adapter = mSectionsPagerAdapter
        recipe_container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(recipe_container))
        tabs.visibility = View.VISIBLE

        topMenu?.getItem(0)?.isVisible = true
        progress_bar_recipe_activity.visibility = View.GONE
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_recipe, menu)
        topMenu = menu

        if (db.FavouritesDataDao().getFavouritesDataWithUrl(recipeUrl!!) != null) {
            val i = menu.getItem(0)

            i.title = getString(R.string.favorite_heart_title)
            i.icon = getDrawable(R.drawable.favorite_heart)
        }
        return true
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_in_favorites) {
            if (item.title == getString(R.string.favorite_heart_title)) {
                /**
                 * RM FROM DATABASE
                 */
                item.title = getString(R.string.heart_title)
                item.icon = getDrawable(R.drawable.heart)

                db.FavouritesDataDao().deleteFavouritesDataWithUrl(recipeUrl!!)
                //Toast.makeText(this, "success remove recipe from database", Toast.LENGTH_LONG).show()
            } else {
                /**
                 * ADD IN DATABASE
                 */
                item.title = getString(R.string.favorite_heart_title)
                item.icon = getDrawable(R.drawable.favorite_heart)

                val fd = FavouritesData()
                fd.recipeId = recipeDataId
                fd.recipeUrl = recipeUrl!!

                db.FavouritesDataDao().insert(fd)
                //Toast.makeText(this, "success insert recipe in database", Toast.LENGTH_LONG).show()

            }

            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private inner class SectionsPagerAdapter(fm: FragmentManager,private val pagesCount: Int) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            val bundle = Bundle()
            bundle.putInt(getString(R.string.recipeDataId), recipeDataId)

            val f: Fragment = when (position) {
                pagesCount - 3 -> {
                    fragmentIsExists(getString(R.string.aboutFragment)) ?: AboutFragment()

                }
                pagesCount - 2 -> {
                    fragmentIsExists(getString(R.string.ingredientsFragment)) ?: IngredientsFragment()
                }

                pagesCount - 1-> {
                    fragmentIsExists(getString(R.string.stepsFragment)) ?: StepsFragment()
                }

                else -> return Fragment()
            }

            f.arguments = bundle
            return f
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

    override fun onDestroy() {

        super.onDestroy()
    }

    override fun onBackPressed() {
        val i = Intent()
        val flag = db.FavouritesDataDao().getFavouritesDataWithUrl(recipeUrl!!) != null
        i.putExtra(getString(R.string.recipesString), flag)
        setResult(Activity.RESULT_OK, i)

        super.onBackPressed()
    }
}