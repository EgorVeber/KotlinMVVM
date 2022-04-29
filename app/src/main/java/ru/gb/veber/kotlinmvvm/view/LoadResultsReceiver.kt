package ru.gb.veber.kotlinmvvm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.widget.Toast
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.view.fragment.*

interface showWeather {
    fun displayWeather(weatherDTO: WeatherDTO)
    fun displayError(string: String)
}

class LoadResultsReceiver(private val listener: showWeather?) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action.equals(BROADCAST_OBSERVER)) {
                listener?.let { listener ->
                    listener.apply {
                        when (it.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                            DETAILS_INTENT_EMPTY_EXTRA -> displayError(DETAILS_INTENT_EMPTY_EXTRA)
                            DETAILS_DATA_EMPTY_EXTRA -> displayError(DETAILS_DATA_EMPTY_EXTRA)
                            DETAILS_RESPONSE_EMPTY_EXTRA -> displayError(
                                DETAILS_RESPONSE_EMPTY_EXTRA
                            )
                            DETAILS_REQUEST_ERROR_EXTRA -> displayError(DETAILS_REQUEST_ERROR_EXTRA)
                            DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> displayError(
                                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA
                            )
                            DETAILS_URL_MALFORMED_EXTRA -> displayError(DETAILS_URL_MALFORMED_EXTRA)
                            DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                                it.getParcelableExtra<WeatherDTO>(KEY_WEATHER_DTO)?.let { weather ->
                                    displayWeather(weather)
                                } ?: run {
                                    displayError("EMPTY weather")
                                }
                            }
                            else -> {}
                        }
                    }
                }
            } else {
                StringBuilder().apply {
                    append("Message from system:\n")
                    append("Connection change")
                    toString().also { message ->
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}