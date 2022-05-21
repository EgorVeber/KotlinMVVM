package ru.gb.veber.kotlinmvvm.room

import android.app.Application
import androidx.room.Room
import java.lang.IllegalStateException

class App : Application() {

    companion object CreateDB {
        private var appInstance: App? = null
        private var weatherDB: WeatherDataBase? = null
        private const val DB_NAME = "WeatherDataBase.db"

        fun getHistoryDao(): HistoryDao {
            if (weatherDB == null) {
                synchronized(WeatherDataBase::class.java)
                {
                    if (weatherDB == null) {
                        if (appInstance == null) throw IllegalStateException("Application is null")
                        weatherDB = Room.databaseBuilder(
                            appInstance!!.applicationContext,
                            WeatherDataBase::class.java,
                            DB_NAME
                        ).allowMainThreadQueries().build()
                    }
                }
            }
            return weatherDB!!.historyDao()
        }
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this
    }
}