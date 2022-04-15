package ru.gb.veber.kotlinmvvm.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl
import java.lang.Thread.sleep
import java.util.*

class ViewModelWeather
    (private val liveDataToObserver: MutableLiveData<AppState> = MutableLiveData(),
    private val repo:Repo= RepoImpl())
    :ViewModel()
{
        fun getLiveData() = liveDataToObserver
        fun getWeather() = getFromSource()
        private fun getFromSource()
        {
            Thread{
                when(Random().nextInt(3)+1)
                {
                    1-> {
                        liveDataToObserver.postValue(AppState.Loading)
                        sleep(1000)
                        liveDataToObserver.postValue(AppState.Error(Throwable()))
                    }
                    2->
                    {
                        liveDataToObserver.postValue(AppState.Loading)
                        sleep(10000)
                        liveDataToObserver.postValue(AppState.Success(repo.getWeatherFromLocalStorage()))
                    }
                    3->
                    {
                        liveDataToObserver.postValue(AppState.Loading)
                        sleep(2000)
                        liveDataToObserver.postValue(AppState.Success(repo.getWeatherFromLocalStorage()))
                    }
                }
            }.start()
        }
}
