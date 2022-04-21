package ru.gb.veber.kotlinmvvm.model
import java.util.*

fun getTemp() = Random().nextInt(20) - 5

fun getWorldCities() = listOf(
    Weather(City("Лондон", 51.5085300, -0.1257400), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Токио", 35.6895000, 139.6917100), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Париж", 48.8534100, 2.3488000), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Берлин", 52.52000659999999, 13.404953999999975), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Рим", 41.9027835, 12.496365500000024), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Минск", 53.90453979999999, 27.561524400000053), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Стамбул", 41.0082376, 28.97835889999999), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Вашингтон", 38.9071923, -77.03687070000001), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Киев", 50.4501, 30.523400000000038), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Пекин", 39.90419989999999, 116.40739630000007), getTemp(), getTemp(), forecastList = getForecastsList())
)

fun getRussianCities() = listOf(
    Weather(City("Москва", 55.755826, 37.617299900000035), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Нижний Новгород", 56.2965039, 43.936059), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Казань", 55.8304307, 49.06608060000008), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Челябинск", 55.1644419, 61.4368432), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Омск", 54.9884804, 73.32423610000001), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Ростов-на-Дону", 47.2357137, 39.701505), getTemp(), getTemp(), forecastList = getForecastsList()),
    Weather(City("Уфа", 54.7387621, 55.972055400000045), getTemp(), getTemp(), forecastList = getForecastsList())
)

fun getHoursList() = listOf(
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
    Hours(Date().hours, Random().nextInt(10) - 5),
)

fun getForecastsList() = listOf(
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList()),
    Forecasts(Date(), Random().nextInt(20) - 10, Random().nextInt(20) - 5, getHoursList())
)
