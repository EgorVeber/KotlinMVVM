package ru.gb.veber.kotlinmvvm.room

import androidx.room.Entity
import androidx.room.PrimaryKey

const val ID = "id"
const val CITY = "city"
const val TEMPERATURE = "temperature"
@Entity
data class HistorySelect(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    val city: String,
    val temperature: Int,
    val condition: String,
    val lat: Double,
    val lot: Double,
    val date: String
)