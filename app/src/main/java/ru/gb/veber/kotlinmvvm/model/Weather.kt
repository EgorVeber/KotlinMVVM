package ru.gb.veber.kotlinmvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Weather(
    val city: City = City(), val temperature: Int = 10
) : Parcelable

@Parcelize
data class City(val cityName: String = "Москва", val lat: Double = 55.0, val lon: Double = 55.0) :
    Parcelable
