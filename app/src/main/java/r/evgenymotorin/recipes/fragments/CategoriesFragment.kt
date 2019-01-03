package r.evgenymotorin.recipes.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.categories_fragment.*
import r.evgenymotorin.recipes.CategoryActivity
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.di.fragment.BaseFragment


const val CATEGORIES_LOG = "categories_log"
class CategoriesFragment : BaseFragment(), View.OnClickListener {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.categories_fragment, container, false)

        r.findViewById<CardView>(R.id.dessert_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.main_food_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.meat_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.salad_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.soup_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.bakery_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.snacks_category).setOnClickListener(this)
        r.findViewById<CardView>(R.id.drinks_category).setOnClickListener(this)

        return r
    }

    override fun onClick(v: View?) {
        /*val category: String = when (v?.id) {
            R.id.dessert_category -> "tags%5Brecipe_category%5D%5B%5D=%D0%B4%D0%B5%D1%81%D0%B5%D1%80%D1%82%D1%8B"
            R.id.main_food_category -> "tags%5Brecipe_category%5D%5B%5D=%D0%BE%D1%81%D0%BD%D0%BE%D0%B2%D0%BD%D1%8B%D0%B5+%D0%B1%D0%BB%D1%8E%D0%B4%D0%B0"
            R.id.meat_category -> "tags%5Brecipe_category%5D%5B%5D=%D1%88%D0%B0%D1%88%D0%BB%D1%8B%D0%BA"
            R.id.salad_category -> "tags%5Brecipe_category%5D%5B%5D=%D1%81%D0%B0%D0%BB%D0%B0%D1%82%D1%8B+%D0%B8+%D0%B2%D0%B8%D0%BD%D0%B5%D0%B3%D1%80%D0%B5%D1%82%D1%8B"
            R.id.soup_category -> "tags%5Brecipe_category%5D%5B%5D=%D1%81%D1%83%D0%BF%D1%8B+%D0%B8+%D0%B1%D1%83%D0%BB%D1%8C%D0%BE%D0%BD%D1%8B"
            R.id.bakery_category -> "tags%5Brecipe_category%5D%5B%5D=%D1%85%D0%BB%D0%B5%D0%B1&tags%5Brecipe_category%5D%5B%5D=%D0%B2%D1%8B%D0%BF%D0%B5%D1%87%D0%BA%D0%B0"
            R.id.snacks_category -> "tags%5Brecipe_category%5D%5B%5D=%D0%B7%D0%B0%D0%BA%D1%83%D1%81%D0%BA%D0%B8&tags%5Brecipe_category%5D%5B%5D=%D0%B1%D1%83%D1%82%D0%B5%D1%80%D0%B1%D1%80%D0%BE%D0%B4%D1%8B"
            R.id.drinks_category -> "tags%5Brecipe_category%5D%5B%5D=%D0%BD%D0%B0%D0%BF%D0%B8%D1%82%D0%BA%D0%B8"
            else -> {
                Toast.makeText(activity, "URL error !", Toast.LENGTH_LONG).show()
                return
            }
        }*/

        val category: String = when (v?.id) {
            R.id.dessert_category -> getString(R.string.dessert_category)
            R.id.main_food_category -> getString(R.string.main_food_category)
            R.id.meat_category -> getString(R.string.meat_category)
            R.id.salad_category -> getString(R.string.salad_category)
            R.id.soup_category -> getString(R.string.soup_category)
            R.id.bakery_category -> getString(R.string.bakery_category)
            R.id.snacks_category -> getString(R.string.snacks_category)
            R.id.drinks_category -> getString(R.string.drinks_category)
            else -> {
                Toast.makeText(activity, "view id error !", Toast.LENGTH_LONG).show()
                return
            }
        }

        val i = Intent(activity!!, CategoryActivity::class.java )

        i.putExtra(getString(R.string.category_name), category)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}