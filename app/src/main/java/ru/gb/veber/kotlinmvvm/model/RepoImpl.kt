package ru.gb.veber.kotlinmvvm.model

class RepoImpl : Repo{
    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
    override fun getWeatherFromLocalStorage(): Weather {
        return Weather()
    }
}