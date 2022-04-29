package ru.gb.veber.kotlinmvvm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.model.showSnackBarError
import ru.gb.veber.kotlinmvvm.view.fragment.*
import ru.gb.veber.kotlinmvvm.view_model.SelectState

interface showWeather {
    fun displayWeather(weatherDTO: WeatherDTO)
    fun displayError(string: String)
}

class LoadResultsReceiver(private val listener: showWeather?) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
            DETAILS_INTENT_EMPTY_EXTRA -> listener?.displayError(DETAILS_INTENT_EMPTY_EXTRA)
            DETAILS_DATA_EMPTY_EXTRA -> listener?.displayError(DETAILS_DATA_EMPTY_EXTRA)
            DETAILS_RESPONSE_EMPTY_EXTRA -> listener?.displayError(DETAILS_RESPONSE_EMPTY_EXTRA)
            DETAILS_REQUEST_ERROR_EXTRA -> listener?.displayError(DETAILS_REQUEST_ERROR_EXTRA)
            DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> listener?.displayError(
                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA
            )
            DETAILS_URL_MALFORMED_EXTRA -> listener?.displayError(DETAILS_URL_MALFORMED_EXTRA)
            DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                intent.getParcelableExtra<WeatherDTO>(KEY_WEATHER_DTO)?.let {
                    listener?.displayWeather(it)
                } ?: run {
                    listener?.displayError("EMPTY weather")
                }
            }
        }
    }
}