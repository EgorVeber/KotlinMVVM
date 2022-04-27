package ru.gb.veber.kotlinmvvm.model

import android.os.Build
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ru.gb.veber.kotlinmvvm.BuildConfig
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.lang.Thread.sleep
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

class WeatherLoader(
    private val listener: MutableLiveData<SelectState>,
    private val lat: Double,
    private val lon: Double
) {

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather() =
        try {
            val uri = URL("https://api.weather.yandex.ru/v2/forecast?lat=${lat}&lon=${lon}")
            val handler = Handler()

            Thread {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = (uri.openConnection() as HttpsURLConnection).apply {
                        requestMethod = "GET"
                        readTimeout = 10000
                        addRequestProperty("X-Yandex-API-Key", BuildConfig.WEATHER_API_KEY)
                    }

                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    val response = getLines(bufferedReader)

                    val weatherDTO: WeatherDTO = Gson().fromJson(response, WeatherDTO::class.java)

                    handler.post {
                        listener.postValue(SelectState.Success(weatherDTO))
                    }

                } catch (e: Exception) {
                    listener.postValue(SelectState.Error(Throwable(), e.message.toString()))
                    e.printStackTrace()
                } finally {
                    urlConnection.disconnect()
                }
            }.start()
        } catch (e: MalformedURLException) {
            listener.postValue(SelectState.Error(Throwable(), e.message.toString()))
            e.printStackTrace()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }
}