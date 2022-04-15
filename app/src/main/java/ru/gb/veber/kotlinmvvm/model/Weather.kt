package ru.gb.veber.kotlinmvvm.model

data class City(val cityName:String="City", val lat:Double=55.0, val lon:Double=55.0)
data class Weather(
    val city:City=City(), val temperature:Int= 0,val feelsLike:Int= 0,
    val condition:String="clear",val icon:String="",val humidity:Int=50
)
