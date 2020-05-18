package br.com.currencyconverter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.currencyconverter.R
import br.com.currencyconverter.extras.*
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.model.History
import br.com.currencyconverter.viewmodels.HistoryViewModel
import br.com.currencyconverter.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_history.*
import javax.inject.Inject


class HistoryFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): Fragment(R.layout.fragment_history){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val viewModel: HistoryViewModel by viewModels {
        viewModelFactory
    }

    lateinit var adapter:HistoryAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.setNavigationOnClickListener{  navController().popBackStack() }
        adapter = HistoryAdapter()
        progress.isVisible = false
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }



        observe(viewModel.data,this::onLoadData)
        observe(viewModel.loading,this::onLoading)
        observe(viewModel.message,this::onMessage)
        requireActivity().hideKeyboard()

        date.text = getString(R.string.date)
        valueA.text = arguments?.getString("currencyA","")
        valueB.text = arguments?.getString("currencyB","")
        eur.text = "${Constants.EUR} ${arguments?.getString("amount","")}"
    }

    override fun onStart() {
        super.onStart()
        viewModel.load(arguments?.getString("currencyA","") ?: "",
            arguments?.getString("currencyB","")?:"",
            arguments?.getString("amount","")?:"")
    }

    private fun onLoadData(data: List<History>) {
        adapter.submitList(data)
    }

    private fun onLoading(data: Boolean) {
        progress.isVisible = data
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(),data,Toast.LENGTH_LONG).show()
    }

    fun navController() = findNavController()
}