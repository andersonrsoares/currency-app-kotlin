package br.com.currencyconverter.ui

sealed class ViewState<out T> {

    data class Success<out T>(val value: T): ViewState<T>()

    data class Error(
        val errorMessage: String? = null
    ): ViewState<Nothing>()


}


