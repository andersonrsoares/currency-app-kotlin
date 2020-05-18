package br.com.currencyconverter.model


import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["date", "name"])
data class Currency(

    var date: String = "",

    var datetime: Long = 0,
    var value: Double = 0.0,
    var name: String = "",

    var base: String = "",

    @Ignore
    var calculated: Double = 0.0,

    @Ignore
    var selected: Boolean = false


)
