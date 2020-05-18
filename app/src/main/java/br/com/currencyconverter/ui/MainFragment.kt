package br.com.currencyconverter.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDestination
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.currencyconverter.R
import br.com.currencyconverter.extras.MoneyTextWatcher
import br.com.currencyconverter.extras.observe
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.viewmodels.MainViewModel
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment
@Inject
constructor(
    private val viewModelFactory: ViewModelProvider.Factory
): Fragment(R.layout.fragment_main),Toolbar.OnMenuItemClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    val viewModel: MainViewModel by viewModels {
        viewModelFactory
    }

    lateinit var adapter:CurrencyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.inflateMenu(R.menu.menu_history)
        toolbar.setOnMenuItemClickListener(this@MainFragment)
        adapter = CurrencyAdapter()
        progress.isVisible = false
        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(requireContext()).apply {
            orientation = LinearLayoutManager.VERTICAL
        }

        textValue.addTextChangedListener(MoneyTextWatcher(textValue))

        textValue.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.convertCurrency(s.toString())
            }
        })

        observe(viewModel.data,this::onLoadData)
        observe(viewModel.loading,this::onLoading)
        observe(viewModel.message,this::onMessage)
        observe(adapter.selected,this::onSelected)


    }

    override fun onStart() {
        super.onStart()
        viewModel.convertCurrency(textValue.text.toString())
    }


    private fun onSelected(data: Boolean) {
        toolbar.menu.getItem(0).isVisible = adapter.currentList.filter { it.selected }.size >= 2

    }
    private fun onLoadData(data: List<Currency>) {
        adapter.submitList(data)
    }

    private fun onLoading(data: Boolean) {
        progress.isVisible = data
    }

    private fun onMessage(data: String) {
        Toast.makeText(requireContext(),data,Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_history, menu)
    }


    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.menu_his -> {
                val list =  adapter.currentList.filter { it.selected }
                if(list.size >= 2){
                    var bundle = bundleOf("amount" to textValue.text.toString(),
                        "currencyA" to list[0].name,
                        "currencyB" to list[1].name)


                    navController().navigate(R.id.action_mainFragment_to_historyFragment,bundle)
                }

                true
            }
            else -> false
        }
    }


    fun navController() = findNavController()
}