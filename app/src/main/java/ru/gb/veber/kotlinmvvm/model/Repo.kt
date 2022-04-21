package ru.gb.veber.kotlinmvvm.model

interface Repo {
    fun getWeatherFromServer():Weather
    fun getWeatherFromLocalStorageRus(): List<Weather>
    fun getWeatherFromLocalStorageWorld(): List<Weather>
}
class RepoImpl : Repo{
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
    override fun getWeatherFromLocalStorageRus(): List<Weather> {
        return  getRussianCities()
    }
    override fun getWeatherFromLocalStorageWorld(): List<Weather> {
        return getWorldCities()
    }
}