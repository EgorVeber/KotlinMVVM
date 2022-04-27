package ru.gb.veber.kotlinmvvm.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl
import ru.gb.veber.kotlinmvvm.model.WeatherDTO

class ViewModelWeatherServer(
    private val liveDataToObserver: MutableLiveData<WeatherDTO> = MutableLiveData(),
    private val repositoryImpl: Repo = RepoImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserver

    @RequiresApi(Build.VERSION_CODES.N)
    fun getServerWeather(lat: Double, lon: Double) {
        //WeatherLoader(liveDataToObserver, lat, lon).loadWeather()
        repositoryImpl.getWeatherFromServer(liveDataToObserver, lat, lon)
    }
}