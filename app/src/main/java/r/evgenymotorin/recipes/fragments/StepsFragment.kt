package r.evgenymotorin.recipes.fragments

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import r.evgenymotorin.recipes.R
import r.evgenymotorin.recipes.RecipeActivity
import r.evgenymotorin.recipes.di.fragment.BaseFragment
import r.evgenymotorin.recipes.model.Step
import r.evgenymotorin.recipes.parsing.Parsers
import r.evgenymotorin.recipes.rows.StepRow

class StepsFragment: BaseFragment() {
    private var steps: ArrayList<Step>? = null
    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.steps_recipe_fragment, container, false)
        r.findViewById<RecyclerView>(R.id.steps_recyclerview_recipe_fragment).adapter = adapter
        return r
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (RecipeActivity.pageHTML == null) {
            Toast.makeText(activity, "page == null", Toast.LENGTH_LONG).show()
            return
        }

        steps= Parsers().scrapStepsFromHTML(RecipeActivity.pageHTML!!)

        if (steps != null && steps!!.isNotEmpty())
            for (i in 0..(steps!!.size - 1)) {
                adapter.add(StepRow("Шаг ${i + 1}", steps!![i]))
            }

    }
}