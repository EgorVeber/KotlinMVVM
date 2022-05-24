package ru.gb.veber.kotlinmvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = City(), val temperature: Int = 0,
    val feelsLike: Int = 0,
    val condition: String = "sunny"
) : Parcelable

@Parcelize
data class City(val cityName: String = "Москва", val lat: Double = 55.0, val lon: Double = 55.0) :
    Parcelable
