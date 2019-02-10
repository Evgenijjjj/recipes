package r.evgenymotorin.recipes.settings

import android.os.Build
import android.os.Bundle
import android.preference.PreferenceFragment
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import r.evgenymotorin.recipes.R

class ActualPreferenceFragment: PreferenceFragment() {
    var callback: (()->Unit)? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.actual_settings_fragment, container, false)

        v.findViewById<FloatingActionButton>(R.id.save_btn_actual_settings_fragment).setOnClickListener {
            callback?.invoke()

            for (f in activity.fragmentManager.fragments) {
                activity.fragmentManager.beginTransaction().remove(f).commit()
            }
        }

        return v
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()

        fragmentManager.beginTransaction()
            .replace(R.id.settings_fragment_root, ActualFiltersActivity.FiltersPreferenceFragment())
            .commit()
    }
}