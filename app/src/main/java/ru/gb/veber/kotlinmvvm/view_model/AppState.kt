package ru.gb.veber.kotlinmvvm.view_model

import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.room.HistorySelect

sealed class AppState {
    data class Success(val weatherList: List<Weather>) : AppState()
    data class SuccessHistory(val weatherList: List<HistorySelect>) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}

