package ru.gb.veber.kotlinmvvm.model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData

interface Repo {
    fun getWeatherFromServer(
        liveDataToObserver: MutableLiveData<WeatherDTO>,
        lat: Double,
        lon: Double
    )

    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}

class RepoImpl : Repo {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun getWeatherFromServer(
        liveDataToObserver: MutableLiveData<WeatherDTO>,
        lat: Double,
        lon: Double
    ) {
        WeatherLoader(liveDataToObserver, lat, lon).loadWeather()
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}