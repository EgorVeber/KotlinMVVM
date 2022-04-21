package ru.gb.veber.kotlinmvvm

import ru.gb.veber.kotlinmvvm.model.City
import ru.gb.veber.kotlinmvvm.model.Weather

 var weatherData: List<Weather> = listOf(
    Weather(City(),12),
    Weather(City(),10),
    Weather(City(),13),
    Weather(City(),13),
    Weather(City(),13),
    Weather(City(),13),
    Weather(City(),10),
    Weather(City(),9),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),10),
    Weather(City(),9),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),10),
    Weather(City(),9),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
    Weather(City(),12),
)
var weatherWeek: List<Weather> = listOf(
   Weather(City(), forecastDate = "Понедельник", tempMin = 10, tempMax = 15),
   Weather(City(), forecastDate = "Вторник", tempMin = 11, tempMax = 17),
   Weather(City(), forecastDate = "Среда", tempMin = 23, tempMax = 13),
   Weather(City(), forecastDate = "Четверг", tempMin = 12, tempMax = 33),
   Weather(City(), forecastDate = "Пятница", tempMin = 31, tempMax = 22),
   Weather(City(), forecastDate = "Субббота", tempMin = 11, tempMax = 11),
   Weather(City(), forecastDate = "Воскресенье", tempMin = 12, tempMax = 13),
)


