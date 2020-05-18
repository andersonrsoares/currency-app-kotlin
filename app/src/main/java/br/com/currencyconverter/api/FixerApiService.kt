package br.com.currencyconverter.api


import retrofit2.http.*
import javax.inject.Singleton

@Singleton
interface FixerApiService {

    @GET("latest?symbols=USD,JPY,GBP,AUD,CAD,CHF,CNY,SEK,NZD&base=EUR")
    suspend fun latest(): ConvertResponse

    @GET("{date}")
    suspend fun byDateAndCurrency(@Path("date") date:String,
                                  @Query("symbols") symbols:String,
                                  @Query("base") base:String): ConvertResponse

}
