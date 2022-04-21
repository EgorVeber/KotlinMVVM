package ru.gb.veber.kotlinmvvm.view_model

import ru.gb.veber.kotlinmvvm.model.Weather

sealed class AppState
{
    data class Success(val weatherData: Weather):AppState()
    data class Error(val error:Throwable):AppState()
    object Loading : AppState()
}

