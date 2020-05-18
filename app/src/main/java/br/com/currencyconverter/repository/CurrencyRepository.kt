package br.com.currencyconverter.repository


import androidx.lifecycle.LiveData
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.model.History
import br.com.currencyconverter.ui.ViewState
import javax.inject.Singleton


@Singleton
interface CurrencyRepository {

    fun convertCurrencyFromEUR(amount:Float):LiveData<ViewState<List<Currency>>>

    fun history(currencyA: String,currencyB: String,amount:Float):LiveData<ViewState<List<History>>>
}
