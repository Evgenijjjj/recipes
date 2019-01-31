package r.evgenymotorin.recipes.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.about_recipe_fragment.*
import kotlinx.android.synthetic.main.image_fragment.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.database.tables.AboutImageData
import r.evgenymotorin.recipes.db.tables.RecipeData
import r.evgenymotorin.recipes.di.base.BaseFragment


const val ABOUT_LOG = "about_log"

class AboutFragment : BaseFragment() {
    private var imageViewPagerAdapter: ImageViewPagerAdapter? = null

    companion object {
        var aboutImageDataList: List<AboutImageData>? = null
    }

    private var recipeData: RecipeData? = null

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.about_recipe_fragment, container, false)

        if (aboutImageDataList != null && aboutImageDataList!!.isNotEmpty()) {
            imageViewPagerAdapter = ImageViewPagerAdapter(childFragmentManager)
            v.findViewById<ViewPager>(R.id.image_view_pager_about_recipe_fragment).adapter = imageViewPagerAdapter
            v.findViewById<ProgressBar>(R.id.progress_about_recipe_fragment).progress = 1
            v.findViewById<ProgressBar>(R.id.progress_about_recipe_fragment).max = aboutImageDataList!!.size
            v.findViewById<TextView>(R.id.page_status_about_recipe_fragment).text = "1/${aboutImageDataList!!.size}"
        } else {
            v.findViewById<CardView>(R.id.img_box_about_recipe_fragment).visibility = View.GONE
        }

        if (!recipeData?.aboutDescription.isNullOrEmpty())
            v.findViewById<TextView>(R.id.description_about_recipe_fragment).text = recipeData!!.aboutDescription
        else
            v.findViewById<CardView>(R.id.description_box_about_recipe_fragment).visibility = View.GONE

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeId = arguments?.getInt(getString(R.string.recipeDataId))

        if (recipeId == null) {
            Toast.makeText(activity, "recipeID == null", Toast.LENGTH_LONG).show()
            return
        }

        recipeData = db.RecipeDataDao().getRecipeWithId(recipeId)
        aboutImageDataList = dbHelper.getAllAboutImagesForRecipeData(recipeData!!)
    }

    override fun onStart() {
        super.onStart()

        image_view_pager_about_recipe_fragment.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onPageSelected(p0: Int) {
                progress_about_recipe_fragment.progress = p0 + 1
                page_status_about_recipe_fragment.text = "${p0 + 1}/${aboutImageDataList?.size}"
            }
        })
    }


    private inner class ImageViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment? {
            if (p0 < aboutImageDataList!!.size) {
                return ImageFragment().newInstance(p0)
            }
            return null
        }

        override fun getCount(): Int {
            return aboutImageDataList!!.size
        }
    }

    class ImageFragment : BaseFragment() {
        private var position: Int = 0
        private val strPosition = "position"

        fun newInstance(pos: Int): ImageFragment {
            val f = ImageFragment()
            val b = Bundle()

            b.putInt(strPosition, pos)
            f.arguments = b
            return f
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            position = arguments?.getInt(strPosition) ?: 0
        }

        override fun onStart() {
            super.onStart()
            if (aboutImageDataList!!.isNotEmpty()) {
                Picasso.get().load(aboutImageDataList!![position].imageUrl).into(image_image_fragment)
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.image_fragment, container, false)
        }
    }
}