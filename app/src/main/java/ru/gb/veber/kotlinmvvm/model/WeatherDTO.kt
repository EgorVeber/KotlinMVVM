package ru.gb.veber.kotlinmvvm.model

data class WeatherDTO(
    val fact: FactDTO,
    val now_dt: String
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?
)