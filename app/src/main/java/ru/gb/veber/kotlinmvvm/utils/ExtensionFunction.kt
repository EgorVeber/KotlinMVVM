package ru.gb.veber.kotlinmvvm.model

import java.text.SimpleDateFormat
import java.util.*

const val FORMAT_DATE = "E, dd MMMM H:m"
const val FORMAT_DATE_DTO = "yyyy-MM-dd"
const val FORMAT_DATE_HISTORY = "MM-dd"
const val FORMAT_HOUR = "H:m"
const val FORMAT_WEEK = "EEEE"
const val WEATHER_URL_ICON = "https://yastatic.net/weather/i/icons/funky/dark/"

fun Date.formatDate(): String = SimpleDateFormat(FORMAT_DATE, Locale.getDefault()).format(this)
fun Date.formatHour(): String = SimpleDateFormat(FORMAT_HOUR, Locale.getDefault()).format(this)
fun Date.formatWeek(): String = SimpleDateFormat(FORMAT_WEEK, Locale.getDefault()).format(this)
fun Date.formatHistory(): String =
    SimpleDateFormat(FORMAT_DATE_HISTORY, Locale.getDefault()).format(this)

fun dayWeekFromString(dateString: String) =
    SimpleDateFormat(FORMAT_DATE_DTO, Locale.getDefault()).parse(dateString)

fun String.addDegree(): String = this + "\u00B0"

fun getSlash(min: Int, max: Int) = min.toString().addDegree() + "/" + max.toString().addDegree()
