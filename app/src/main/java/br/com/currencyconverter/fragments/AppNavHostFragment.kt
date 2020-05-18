package br.com.currencyconverter.fragments

import android.content.Context
import android.os.Bundle
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment
import br.com.currencyconverter.ui.MainActivity


class AppNavHostFragment : NavHostFragment(){

    override fun onAttach(context: Context) {
        childFragmentManager.fragmentFactory =
            (activity as MainActivity).fragmentFactory
        super.onAttach(context)
    }

    companion object{

        private const val KEY_GRAPH_ID = "android-support-nav:fragment:graphId"

        @JvmStatic
        fun create(
            @NavigationRes graphId: Int = 0
        ): AppNavHostFragment {
            var bundle: Bundle? = null
            if(graphId != 0){
                bundle = Bundle()
                bundle.putInt(KEY_GRAPH_ID, graphId)
            }
            val result =
                AppNavHostFragment()
            if(bundle != null){
                result.arguments = bundle
            }
            return result
        }
    }
}
