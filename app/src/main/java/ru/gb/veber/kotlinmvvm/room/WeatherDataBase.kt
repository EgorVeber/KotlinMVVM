package ru.gb.veber.kotlinmvvm.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(HistorySelect::class), version = 1, exportSchema = false)
abstract class WeatherDataBase : RoomDatabase() {

    abstract fun historyDao(): HistoryDao
}