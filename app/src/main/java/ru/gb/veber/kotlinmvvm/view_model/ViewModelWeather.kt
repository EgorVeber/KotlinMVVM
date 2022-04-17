package ru.gb.veber.kotlinmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl
import java.lang.Thread.sleep
import java.util.*

class ViewModelWeather(
    private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repositoryImpl: Repo = RepoImpl()) : ViewModel() {

    fun getLiveData() = liveDataToObserver
    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)
    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserver.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserver.postValue(
                AppState.Success(
                    if (isRussian) repositoryImpl.getWeatherFromLocalStorageRus()
                    else repositoryImpl.getWeatherFromLocalStorageWorld()
                )
            )
        }.start()
    }
}
