package br.com.currencyconverter.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.currencyconverter.model.Currency
import br.com.currencyconverter.model.History


@Database(entities = [Currency::class,History::class], version = 5)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getCurrencyDao(): CurrencyDao


    companion object{
        const val DATABASE_NAME: String = "app_db"
    }


}








