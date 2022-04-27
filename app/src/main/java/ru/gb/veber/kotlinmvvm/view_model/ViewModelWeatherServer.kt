package ru.gb.veber.kotlinmvvm.view_model

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl

class ViewModelWeatherServer(
    private val liveDataToObserver: MutableLiveData<SelectState> = MutableLiveData(),
    private val repositoryImpl: Repo = RepoImpl()
) : ViewModel() {

    fun getLiveData() = liveDataToObserver

    @RequiresApi(Build.VERSION_CODES.N)
    fun getServerWeather(lat: Double, lon: Double) {
        liveDataToObserver.postValue(SelectState.Loading)
        repositoryImpl.getWeatherFromServer(liveDataToObserver, lat, lon)
    }
}