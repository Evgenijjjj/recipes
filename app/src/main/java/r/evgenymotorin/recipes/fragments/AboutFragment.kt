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
import r.evgenymotorin.recipes.RecipeActivity.Companion.pageHTML
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.model.About
import r.evgenymotorin.recipes.parsing.Parsers


const val ABOUT_LOG = "about_log"

class AboutFragment : BaseFragment() {
    private var imageViewPagerAdapter: ImageViewPagerAdapter? = null

    companion object {
        var about: About? = null
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.about_recipe_fragment, container, false)

        imageViewPagerAdapter = ImageViewPagerAdapter(childFragmentManager)
        v.findViewById<ViewPager>(R.id.image_view_pager_about_recipe_fragment).adapter = imageViewPagerAdapter
        v.findViewById<ProgressBar>(R.id.progress_about_recipe_fragment).progress = 1
        v.findViewById<ProgressBar>(R.id.progress_about_recipe_fragment).max = about?.imgUrlsList?.size!!
        v.findViewById<TextView>(R.id.page_status_about_recipe_fragment).text = "1/${about?.imgUrlsList?.size!!}"
        v.findViewById<TextView>(R.id.description_about_recipe_fragment).text =  about?.description

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
                page_status_about_recipe_fragment.text = "${p0 + 1}/${about?.imgUrlsList?.size!!}"
            }
        })
    }


    private inner class ImageViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
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
            if (about!!.imgUrlsList.isNotEmpty()) {
                Picasso.get().load(about!!.imgUrlsList[position]).into(image_image_fragment)
            }
        }

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            return inflater.inflate(R.layout.image_fragment, container, false)
        }
    }
}