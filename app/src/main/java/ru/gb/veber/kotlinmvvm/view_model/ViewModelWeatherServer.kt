package ru.gb.veber.kotlinmvvm.view_model

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.model.WeatherLoader
import java.lang.Thread.sleep
import java.util.*

class ViewModelWeatherServer(
    private val liveDataToObserver: MutableLiveData<SelectState> = MutableLiveData()) : ViewModel() {

    fun getLiveData() = liveDataToObserver
    private fun getDataFromLocalSource(isRussian: Boolean) {
        liveDataToObserver.value = SelectState.Loading
        Thread {
            sleep(1000)
            liveDataToObserver.postValue(
                SelectState.Success(WeatherDTO())
            )
        }.start()
    }
}
