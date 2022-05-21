package ru.gb.veber.kotlinmvvm.model

import retrofit2.Callback
import ru.gb.veber.kotlinmvvm.repository.RemoteDataSource

interface Repo {
    fun getWeatherFromServer(lat: Double, lon: Double, callback: Callback<WeatherDTO>)
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}

class RepoImpl(private val remoteDataSource: RemoteDataSource?) : Repo {
    override fun getWeatherFromServer(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        remoteDataSource?.getWeatherDetails(lat, lon, callback)
    }

    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}