package br.com.currencyconverter.extras

import br.com.currencyconverter.api.ConvertResponse
import br.com.currencyconverter.api.CurrencyDto
import br.com.currencyconverter.model.Currency
import com.google.gson.*

class Constants {

    companion object{

        const val NETWORK_TIMEOUT = 6000L
        const val NETWORK_ERROR = "Network error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val UNKNOWN_ERROR = "Unknown error"
        const val EUR = "EUR"
    }
}