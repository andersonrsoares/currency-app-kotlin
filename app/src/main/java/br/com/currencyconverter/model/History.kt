package br.com.currencyconverter.model

import androidx.room.Entity
import androidx.room.Ignore


@Entity(primaryKeys = ["date", "nameA", "nameB"])
data class History(
    var base: String = "",

    var date: String = "",

    var datetime: Long = 0,

    var nameA: String = "",
    var nameB: String = "",

    var valueA: Double = 0.0,
    var valueB: Double = 0.0,

    @Ignore
    var calculatedA: Double = 0.0,

    @Ignore
    var calculatedB: Double = 0.0
)
