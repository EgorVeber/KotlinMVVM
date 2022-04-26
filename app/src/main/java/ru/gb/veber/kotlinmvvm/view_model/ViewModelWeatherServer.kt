package ru.gb.veber.kotlinmvvm.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.model.WeatherLoader
import java.lang.Thread.sleep
import java.util.*

class ViewModelWeatherServer(
    private val liveDataToObserver: MutableLiveData<WeatherDTO> = MutableLiveData()
) : ViewModel() {

    fun getLiveData() = liveDataToObserver

    @RequiresApi(Build.VERSION_CODES.N)
    fun getServerWeather(lat: Double, lon: Double) {
        WeatherLoader(liveDataToObserver, lat, lon).loadWeather()
    }
}