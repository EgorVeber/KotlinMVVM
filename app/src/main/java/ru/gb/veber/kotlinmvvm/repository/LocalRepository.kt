package ru.gb.veber.kotlinmvvm.repository

import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.convertHistoryEntityToWeather
import ru.gb.veber.kotlinmvvm.model.convertWeatherToEntity
import ru.gb.veber.kotlinmvvm.room.HistoryDao
import ru.gb.veber.kotlinmvvm.room.HistorySelect

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
    fun deleteHistory()
    fun filterName(string: String): List<HistorySelect>
}

class LocalRepositoryImp(private val localDataSource: HistoryDao) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToEntity(weather))
    }

    override fun deleteHistory() {
        localDataSource.deleteAll()
    }

    override fun filterName(string: String): List<HistorySelect> {
        return localDataSource.getDataByCityName(cityName = string)
    }
}