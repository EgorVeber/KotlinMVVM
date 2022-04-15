package ru.gb.veber.kotlinmvvm.model

import java.util.*

data class City(val cityName:String="Москва", val lat:Double=55.0, val lon:Double=55.0)
data class Weather(
    val city:City=City(), val temperature:Int= 10,val feelsLike:Int= 9,
    val condition:String="Ясно",val icon:String="",val humidity:Int=50,
    val time:Date = Date(),val forecastDate:String="Week",
    val tempMin:Int=10, val tempMax:Int=18
)
