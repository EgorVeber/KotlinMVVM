package ru.gb.veber.kotlinmvvm.repository

import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.convertHistoryEntityToWeather
import ru.gb.veber.kotlinmvvm.model.convertWeatherToEntity
import ru.gb.veber.kotlinmvvm.room.HistoryDao

interface LocalRepository {
    fun getAllHistory(): List<Weather>
    fun saveEntity(weather: Weather)
    fun deleteHistory()
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
}