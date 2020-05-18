package br.com.currencyconverter.fragments

import androidx.fragment.app.FragmentFactory
import androidx.lifecycle.ViewModelProvider
import br.com.currencyconverter.ui.HistoryFragment
import br.com.currencyconverter.ui.MainFragment
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppFragmentFactory
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
) : FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String) =
        when (className) {

            HistoryFragment::class.java.name -> {
                HistoryFragment(viewModelFactory)
            }

            else -> {
                MainFragment(viewModelFactory)
            }
        }


}