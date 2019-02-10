package r.evgenymotorin.recipes.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.categories_fragment.*
import r.evgenymotorin.recipes.ActualRecipesActivity
import r.evgenymotorin.recipes.CategoryActivity
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.di.base.BaseFragment


const val CATEGORIES_LOG = "categories_log"
class CategoriesFragment : BaseFragment(), View.OnClickListener {

    private lateinit var actualRecipesActivityIntent: Intent

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

        r.findViewById<FrameLayout>(R.id.actual_btn_categories_fragment).setOnClickListener {
            it.isEnabled = false
            startActivity(actualRecipesActivityIntent)
        }

        return r
    }

    override fun onClick(v: View?) {
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

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()
        actual_textView_categories_fragment.text = "${getString(R.string.compilation)} ${getCurrentPeriod()}"
        actual_btn_categories_fragment.isEnabled = true
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        actualRecipesActivityIntent = Intent(activity, ActualRecipesActivity::class.java)
    }
}