package ru.gb.veber.kotlinmvvm.view_model


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.gb.veber.kotlinmvvm.model.Repo
import ru.gb.veber.kotlinmvvm.model.RepoImpl
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.repository.LocalRepository
import ru.gb.veber.kotlinmvvm.repository.LocalRepositoryImp
import ru.gb.veber.kotlinmvvm.repository.RemoteDataSource
import ru.gb.veber.kotlinmvvm.room.App.CreateDB.getHistoryDao

private const val REQUEST_ERROR = "Ошибка запроса на сервер"
private const val CORRUPTED_DATA = "Неполные данные"

class ViewModelWeatherServer(
    val detailsLiveData: MutableLiveData<SelectState> = MutableLiveData(),
    private val repositoryImpl: Repo = RepoImpl(RemoteDataSource()),
    private val historyRepo: LocalRepository = LocalRepositoryImp(getHistoryDao())
) : ViewModel() {

    fun saveCityToDB(weather: Weather) {
        Thread {
            historyRepo.saveEntity(weather)
        }.start()
    }

    fun getWeatherFromRemoteSource(lat: Double, lon: Double) {
        detailsLiveData.value = SelectState.Loading
        repositoryImpl.getWeatherFromServer(lat, lon, callback)
    }

    private val callback = object : retrofit2.Callback<WeatherDTO> {
        override fun onResponse(
            call: retrofit2.Call<WeatherDTO>,
            response: retrofit2.Response<WeatherDTO>
        ) {
            val serverResponse: WeatherDTO? = response.body()
            detailsLiveData.postValue(
                if (response.isSuccessful && serverResponse != null) {
                    checkResponse(serverResponse)
                } else {
                    SelectState.Error(Throwable())
                }
            )
        }

        private fun checkResponse(serverResponse: WeatherDTO): SelectState {
            val fact = serverResponse.fact
            return if (fact == null || fact.temp == null || fact.feels_like ==
                null || fact.condition.isNullOrEmpty()
            ) {
                SelectState.Error(Throwable(CORRUPTED_DATA))
            } else {
                SelectState.Success(serverResponse)
            }
        }

        override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
            Log.d("TAG", "onFailure() called with: call = $call, t = $t")
            detailsLiveData.postValue(SelectState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }
}