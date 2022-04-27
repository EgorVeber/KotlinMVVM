package ru.gb.veber.kotlinmvvm.model

data class WeatherDTO(
    val fact: FactDTO? = null,
    val now_dt: String? = null,
    val forecasts: ArrayList<Forecasts> = arrayListOf(),
)

data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?
)

data class Forecasts(
    val date: String,
    val hours: ArrayList<Hours> = arrayListOf(),
    val parts: Parts
)

data class Hours(
    val hour: String? = null,
    val temp: Int? = null,
    val feels_like: Int? = null,
    val icon: String? = null,
    val condition: String? = null,
)

data class Parts(
    var evening: Evening,
    var morning: Morning,
    var night: Night,
    var day: Day,
)

data class Day(
    val temp_min: Int,
    val temp_max: Int
)

data class Night(
    var temp_min: Int,
    var temp_max: Int
)

data class Morning(
    val temp_min: Int,
    val temp_max: Int
)

data class Evening(
    val temp_min: Int,
    val temp_max: Int
)
