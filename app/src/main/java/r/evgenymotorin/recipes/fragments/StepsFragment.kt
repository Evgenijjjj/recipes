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
import r.evgenymotorin.recipes.database.tables.StepData
import r.evgenymotorin.recipes.db.tables.RecipeData
import r.evgenymotorin.recipes.di.base.BaseFragment
import r.evgenymotorin.recipes.model.Step
import r.evgenymotorin.recipes.parsing.Parsers
import r.evgenymotorin.recipes.rows.StepRow

class StepsFragment: BaseFragment() {
    private var recipeData: RecipeData? = null
    private var stepDataList: List<StepData>? = null

    private val adapter = GroupAdapter<ViewHolder>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val r = inflater.inflate(R.layout.steps_recipe_fragment, container, false)
        r.findViewById<RecyclerView>(R.id.steps_recyclerview_recipe_fragment).adapter = adapter
        return r
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val recipeId = arguments?.getInt(getString(R.string.recipeDataId))

        if (recipeId == null) {
            Toast.makeText(activity, "recipeID == null", Toast.LENGTH_LONG).show()
            return
        }

        recipeData = db.RecipeDataDao().getRecipeWithId(recipeId)
        stepDataList = dbHelper.getAllStepsForRecipeData(recipeData!!)

        if (stepDataList != null && stepDataList!!.isNotEmpty())
            for (i in 0..(stepDataList!!.size - 1)) {
                adapter.add(StepRow("Шаг ${i + 1}", stepDataList!![i]))
            }

    }
}