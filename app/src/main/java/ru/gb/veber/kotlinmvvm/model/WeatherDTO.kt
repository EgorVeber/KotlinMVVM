package ru.gb.veber.kotlinmvvm.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WeatherDTO(
    var info: Info? = Info(),
    val fact: FactDTO? = null,
    val now_dt: String? = null,
    val forecasts: ArrayList<Forecasts> = arrayListOf(),
) : Parcelable

@Parcelize
data class Info(
    var lat: Double? = null,
    var lon: Double? = null,
    var tzinfo: Tzinfo? = Tzinfo(),
    var def_pressure_mm: Int? = null,
) : Parcelable

@Parcelize
data class Tzinfo(
    var name: String? = null,
    var abbr: String? = null,
    var dst: Boolean? = null,
    var offset: Int? = null
) : Parcelable

@Parcelize
data class FactDTO(
    val temp: Int?,
    val feels_like: Int?,
    val condition: String?,
    var icon: String?,
    var wind_speed: Double,
    var pressure_mm: Int,
    var humidity: Int,
    var season: String
) : Parcelable

@Parcelize
data class Forecasts(
    val date: String,
    val hours: ArrayList<Hours> = arrayListOf(),
    val parts: Parts,
    var sunrise: String,
    var sunset: String
) : Parcelable

@Parcelize
data class Hours(
    val hour: String? = null,
    val temp: Int? = null,
    val feels_like: Int? = null,
    val icon: String? = null,
    val condition: String? = null,
) : Parcelable

@Parcelize
data class Parts(
    var evening: Evening,
    var morning: Morning,
    var night: Night,
    var day: Day,
) : Parcelable

@Parcelize
data class Day(
    val temp_min: Int,
    val temp_max: Int,
    var icon: String?
) : Parcelable

@Parcelize
data class Night(
    var temp_min: Int,
    var temp_max: Int,
    var icon: String?
) : Parcelable

@Parcelize
data class Morning(
    val temp_min: Int,
    val temp_max: Int,
    var icon: String?
) : Parcelable

@Parcelize
data class Evening(
    val temp_min: Int,
    val temp_max: Int,
    var icon: String?
) : Parcelable
