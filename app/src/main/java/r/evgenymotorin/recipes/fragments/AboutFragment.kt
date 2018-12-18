package r.evgenymotorin.recipes.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.about_recipe_fragment.*
import kotlinx.android.synthetic.main.image_fragment.*
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity.Companion.pageHTML
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.model.About
import r.evgenymotorin.recipes.parsing.Parsers


const val ABOUT_LOG = "about_log"

class AboutFragment: BaseFragment() {
    private var imageViewPagerAdapter: ImageViewPagerAdapter? = null
    private var currentPage = 0

    companion object {
        var about: About? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.about_recipe_fragment, container, false)

        try {
            imageViewPagerAdapter = ImageViewPagerAdapter(childFragmentManager)
            v.findViewById<ViewPager>(R.id.image_view_pager_about_recipe_fragment).adapter = imageViewPagerAdapter
        } catch (e: Exception) {
            Log.d(ABOUT_LOG, "e: $e")
        }

        return v
    }

    override fun onResume() {
        super.onResume()
        image_view_pager_about_recipe_fragment.adapter = imageViewPagerAdapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // Log.d(ABOUT_LOG, "onCreate HTML: $pageHTML")

        if (pageHTML == null) {
            Toast.makeText(activity, "page == null", Toast.LENGTH_LONG).show()
            return
        }

        about = Parsers().scrapAboutInformationFromHTML(pageHTML!!)
    }

    override fun onStart() {
        super.onStart()

        image_view_pager_about_recipe_fragment.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {}

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {}

            override fun onPageSelected(p0: Int) {
                progress_about_recipe_fragment.progress = p0 + 1
            }
        })

        description_about_recipe_fragment.text = about?.description

        progress_about_recipe_fragment.max = about?.imgUrlsList?.size!!
        progress_about_recipe_fragment.progress = 1
    }


    private inner class ImageViewPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment? {
            if (p0 < about?.imgUrlsList?.size!!) {
                return ImageFragment().newInstance(p0)
            }
            return null
        }

        override fun getCount(): Int {
            return about!!.imgUrlsList.size
        }
    }

    class ImageFragment: BaseFragment() {
        private var position: Int = 0

        fun newInstance(pos: Int): ImageFragment {
            val f = ImageFragment()
            val b = Bundle()

            b.putInt(activity?.getString(R.string.position), pos)
            f.arguments = b
            return f
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            position = arguments?.getInt(activity?.getString(R.string.position)) ?: 0
        }

        override fun onStart() {
            super.onStart()
            if (about?.imgUrlsList?.isNotEmpty()!!)
                Picasso.get().load(about!!.imgUrlsList[position]).into(image_image_fragment)
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val v = inflater.inflate(R.layout.image_fragment, container, false)
            return v
        }
    }
}