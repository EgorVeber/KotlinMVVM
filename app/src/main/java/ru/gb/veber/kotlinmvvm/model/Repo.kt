package ru.gb.veber.kotlinmvvm.model

interface Repo {
    fun getWeatherFromServer(): WeatherDTO
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}

class RepoImpl : Repo {
    override fun getWeatherFromServer() = WeatherDTO()
    override fun getWeatherFromLocalStorageRus() = getRussianCities()
    override fun getWeatherFromLocalStorageWorld() = getWorldCities()
}