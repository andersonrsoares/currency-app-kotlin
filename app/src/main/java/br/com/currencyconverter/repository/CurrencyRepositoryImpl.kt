package br.com.currencyconverter.repository


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.currencyconverter.api.*
import br.com.currencyconverter.extras.Constants
import br.com.currencyconverter.extras.calcute
import br.com.currencyconverter.extras.format
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.model.History
import br.com.currencyconverter.persistence.CurrencyDao
import br.com.currencyconverter.ui.ViewState
import kotlinx.coroutines.*
import org.threeten.bp.Instant
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CurrencyRepositoryImpl
@Inject
constructor(
    private val api: FixerApiService,
    private val dao: CurrencyDao
): CurrencyRepository
{
    override fun convertCurrencyFromEUR(amount: Float):LiveData<ViewState<List<Currency>>> {
        val data = MutableLiveData<ViewState<List<Currency>>>()
        GlobalScope.launch(Dispatchers.IO) {

            val time = Instant.now().minusMillis(TimeUnit.MINUTES.toMillis(3)).toEpochMilli()
            val timenow = Instant.now().toEpochMilli()
            val dateforamt = Instant.now().format()
            val list = dao.list(time,dateforamt).calcute(amount)

            if(list.isEmpty()){
                val response = when(val apiResult = safeApiCall(Dispatchers.IO){ api.latest() }){
                    is ApiResult.NetworkError -> {
                        ViewState.Error(apiResult.errorMessage)
                    }
                    is ApiResult.GenericError -> {
                        ViewState.Error(apiResult.errorMessage)
                    }
                    is ApiResult.Success  -> {
                        dao.save(apiResult.value.rates.map { m -> Currency(apiResult.value.date,timenow,m.value,m.name,
                            Constants.EUR) })
                        val response = dao.list(dateforamt).calcute(amount)
                        ViewState.Success(response)
                    }
                }
                GlobalScope.launch(Dispatchers.Main){
                    data.postValue(response)
                }
            }else{
                GlobalScope.launch(Dispatchers.Main){
                    data.postValue(ViewState.Success(list))
                }
            }

        }
        return data
    }

    private suspend fun loadFromAPIAndCache(date:String,currencyA: String,currencyB: String) =
        when (val apiResult = safeApiCall(Dispatchers.IO) { api.byDateAndCurrency(date, "$currencyA,$currencyB",Constants.EUR) }) {
            is ApiResult.Success -> {
                if( apiResult.value.rates.size >=2 ){
                    val a = apiResult.value.rates.first { it.name == currencyA }
                    val b = apiResult.value.rates.first { it.name == currencyB }
                    print(a.name +  " " + a.name + " - " + b.value  + b.value )
                    val c = History(
                        base = apiResult.value.base,
                        date = apiResult.value.date,
                        datetime = Instant.now().toEpochMilli(),
                        nameA =  a.name,
                        nameB =  b.name,
                        valueA =  a.value,
                        valueB =  b.value
                    )

                    dao.saveHistory(c)
                    c
                }else{
                    null
                }


            } else -> {
                null
            }
        }

    override fun history(currencyA: String, currencyB: String,amount:Float): LiveData<ViewState<List<History>>> {
        val data = MutableLiveData<ViewState<List<History>>>()
        GlobalScope.launch(Dispatchers.IO) {

           val async =  arrayListOf(6L,5L,4L,3L,2L,1L).map { i->
                this.async(Dispatchers.IO) {
                    val date = Instant.now().minusMillis(TimeUnit.DAYS.toMillis(i)).format()
                    val history = dao.history(date,currencyA,currencyB)?.let {
                            it
                        } ?: loadFromAPIAndCache(date,currencyA,currencyB)


                    history?.apply {
                        calculatedA = amount * this.valueA
                        calculatedB = amount * this.valueB
                    }
                    history
                }
            }

           val response =  async.awaitAll()

            GlobalScope.launch(Dispatchers.Main){
                data.postValue(ViewState.Success(response.filterNotNull()))
            }
        }
        return data
    }
}





