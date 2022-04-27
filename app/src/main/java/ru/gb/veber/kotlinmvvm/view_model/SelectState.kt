package ru.gb.veber.kotlinmvvm.view_model

import ru.gb.veber.kotlinmvvm.model.WeatherDTO

sealed class SelectState
{
    data class Success(val weatherDTO: WeatherDTO):SelectState()
    data class Error(val error:Throwable,val message:String):SelectState()
    object Loading : SelectState()
}

