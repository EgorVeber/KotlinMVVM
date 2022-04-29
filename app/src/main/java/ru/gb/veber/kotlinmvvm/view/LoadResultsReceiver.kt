package ru.gb.veber.kotlinmvvm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast
import ru.gb.veber.kotlinmvvm.model.WeatherDTO
import ru.gb.veber.kotlinmvvm.view.fragment.*

const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"

interface LoadReceiver {
    fun displayLoadReceiverWeather(weatherDTO: WeatherDTO)
    fun displayLoadReceiverError(string: String)
}

class LoadResultsReceiver(private val listener: LoadReceiver?) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            if (it.action.equals(BROADCAST_OBSERVER)) {
                listener?.let { listener ->
                    listener.apply {
                        when (it.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                            DETAILS_INTENT_EMPTY_EXTRA -> displayLoadReceiverError(DETAILS_INTENT_EMPTY_EXTRA)
                            DETAILS_DATA_EMPTY_EXTRA -> displayLoadReceiverError(DETAILS_DATA_EMPTY_EXTRA)
                            DETAILS_RESPONSE_EMPTY_EXTRA -> displayLoadReceiverError(
                                DETAILS_RESPONSE_EMPTY_EXTRA
                            )
                            DETAILS_REQUEST_ERROR_EXTRA -> displayLoadReceiverError(DETAILS_REQUEST_ERROR_EXTRA)
                            DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> displayLoadReceiverError(
                                DETAILS_REQUEST_ERROR_MESSAGE_EXTRA
                            )
                            DETAILS_URL_MALFORMED_EXTRA -> displayLoadReceiverError(DETAILS_URL_MALFORMED_EXTRA)
                            DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                                it.getParcelableExtra<WeatherDTO>(KEY_WEATHER_DTO)?.let { weather ->
                                    displayLoadReceiverWeather(weather)
                                } ?: run {
                                    displayLoadReceiverError("EMPTY weather")
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