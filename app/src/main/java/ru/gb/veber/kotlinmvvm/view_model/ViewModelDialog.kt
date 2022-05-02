package ru.gb.veber.kotlinmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Info

class ViewModelDialog(private val weatherData: MutableLiveData<Info> = MutableLiveData()) :
    ViewModel() {
    fun setWeatherData(info: Info) {
        weatherData.postValue(info)
    }

    fun getWeatherData(): MutableLiveData<Info> {
        return weatherData
    }
}