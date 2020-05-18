package br.com.currencyconverter.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.currencyconverter.extras.Constants
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.model.History


@Dao
interface CurrencyDao {

    @Query("SELECT * FROM currency where datetime > :datetime and date = :date and base = :base order by name")
    suspend fun list(datetime: Long,date: String,base:String = Constants.EUR): List<Currency>

    @Query("SELECT * FROM currency where date = :date and base = :base  order by name")
    suspend fun list(date: String,base:String = Constants.EUR): List<Currency>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(list: List<Currency>)

    @Query("SELECT * FROM history where date = :date and nameA = :nameA and nameB = :nameB  order by datetime desc limit 1")
    suspend fun history(date: String,nameA:String,nameB:String): History?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveHistory(history: History)
}













