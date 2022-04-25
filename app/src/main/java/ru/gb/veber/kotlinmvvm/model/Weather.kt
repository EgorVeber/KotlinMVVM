package ru.gb.veber.kotlinmvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Weather(
    val city: City = City(), val temperature: Int = 10, val feelsLike: Int = 9,
    val condition: String = "Ясно", val icon: String = "", val humidity: Int = 50,
    val time: Date = Date(),
    val tempMin: Int = 10, val tempMax: Int = 18, val forecastList: List<ForecastsCustom> = listOf()
) : Parcelable

@Parcelize
data class City(val cityName: String = "Москва", val lat: Double = 55.0, val lon: Double = 55.0) : Parcelable

@Parcelize
data class ForecastsCustom(val date: Date = Date(), val temp_min: Int =10, val temp_max: Int= 15, val hours: List<HoursCustom> = listOf()) : Parcelable

@Parcelize
data class HoursCustom(val hour: Date =Date(), val temp: Int=10) : Parcelable