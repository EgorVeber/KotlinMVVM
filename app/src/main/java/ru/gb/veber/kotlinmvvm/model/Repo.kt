package ru.gb.veber.kotlinmvvm.model

interface Repo {
    fun getWeatherFromServer(): Weather
    fun getWeatherFromLocalStorage(): Weather
}