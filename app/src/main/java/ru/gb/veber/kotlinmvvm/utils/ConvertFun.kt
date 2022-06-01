package ru.gb.veber.kotlinmvvm.model

import ru.gb.veber.kotlinmvvm.room.HistorySelect
import java.util.*

fun convertHistoryEntityToWeather(entityList: List<HistorySelect>): List<Weather> {
    return entityList.map {
        Weather(
            City(it.city, it.lat, it.lot), it.temperature, 0,
            it.condition
        )
    }

}

fun convertWeatherToEntity(weather: Weather): HistorySelect {
    return HistorySelect(
        0,
        weather.city.cityName,
        weather.temperature,
        weather.condition,
        weather.city.lat,
        weather.city.lon,
        Date().formatHistory()
    )
}

fun factToWeather(weatherDTO: FactDTO): Weather {
    return Weather(City(), weatherDTO.temp!!, weatherDTO.feels_like!!, weatherDTO.condition!!)
}


