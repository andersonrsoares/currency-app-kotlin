package br.com.currencyconverter.viewmodels

import androidx.lifecycle.*
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.repository.CurrencyRepository
import br.com.currencyconverter.ui.ViewState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel
@Inject
constructor(private val repository: CurrencyRepository)
    : ViewModel()
{
    private var _data = MutableLiveData<List<Currency>>()
    private var _message = MutableLiveData<String>()
    private var _loading = MutableLiveData<Boolean>()

    val data:LiveData<List<Currency>>
        get() = _data

    val message:LiveData<String>
        get() = _message

    val loading:LiveData<Boolean>
        get() = _loading


    private val observer = Observer<ViewState<List<Currency>>>{ res->
        when(res){
            is ViewState.Error -> {
                _message.postValue(res.errorMessage)
            }

            is ViewState.Success  -> {
                _data.postValue(res.value)
            }
        }
        _loading.postValue(false)
    }

    fun convertCurrency(amount:String){

        amount.toFloatOrNull()?.let {
            _loading.postValue(true)

            val source =  repository.convertCurrencyFromEUR(it)
            source.removeObserver(observer)
            source.observeForever(observer)
        } ?: kotlin.run {
            _message.postValue("Value input is wrong")
        }

    }




}