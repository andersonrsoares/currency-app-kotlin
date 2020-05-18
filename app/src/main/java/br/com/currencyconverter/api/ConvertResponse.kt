package br.com.currencyconverter.api


import com.google.gson.annotations.SerializedName

data class ConvertResponse(
    @SerializedName("base")
    var base: String = "",
    @SerializedName("rates")
    var rates: List<CurrencyDto> = arrayListOf()
):BaseResponse()