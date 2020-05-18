package br.com.currencyconverter.api


import com.google.gson.annotations.SerializedName

open class BaseResponse(
    @SerializedName("date")
    var date: String = "",
    @SerializedName("success")
    var success: Boolean = false,
    @SerializedName("timestamp")
    var timestamp: Int = 0
)