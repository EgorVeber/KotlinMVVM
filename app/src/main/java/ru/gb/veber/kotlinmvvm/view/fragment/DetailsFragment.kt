package ru.gb.veber.kotlinmvvm.view.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.ConnectivityManager.CONNECTIVITY_ACTION
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentDetailsBinding
import ru.gb.veber.kotlinmvvm.model.*
import ru.gb.veber.kotlinmvvm.view.LATITUDE_EXTRA
import ru.gb.veber.kotlinmvvm.view.LONGITUDE_EXTRA
import ru.gb.veber.kotlinmvvm.view.SelectService
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer
import java.util.*

const val BROADCAST_OBSERVER = "BROADCAST_OBSERVER"
const val KEY_WEATHER_DTO = "KEY_WEATHER_DTO"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"
const val DETAILS_INTENT_EMPTY_EXTRA = "INTENT IS EMPTY"
const val DETAILS_DATA_EMPTY_EXTRA = "DATA IS EMPTY"
const val DETAILS_RESPONSE_EMPTY_EXTRA = "RESPONSE IS EMPTY"
const val DETAILS_REQUEST_ERROR_EXTRA = "REQUEST ERROR"
const val DETAILS_REQUEST_ERROR_MESSAGE_EXTRA = "REQUEST ERROR MESSAGE"
const val DETAILS_URL_MALFORMED_EXTRA = "URL MALFORMED"
const val DETAILS_RESPONSE_SUCCESS_EXTRA = "RESPONSE SUCCESS"

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapterHour = AdapterHour()
    private val adapterWeek = AdapterWeek()
    private lateinit var weatherBundle: Weather

    private val viewModel: ViewModelWeatherServer by lazy {
        ViewModelProvider(this).get(ViewModelWeatherServer::class.java)
    }

    companion object {
        const val KEY_WEATHER = "KEY_WEATHER"
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.let { SAB ->
            SAB.subtitle = resources.getString(R.string.city)
        }

        weatherBundle = arguments?.getParcelable<Weather>(KEY_WEATHER) ?: Weather()

        view.apply {
            findViewById<RecyclerView>(R.id.list_hour).adapter = adapterHour
            findViewById<RecyclerView>(R.id.list_week).adapter = adapterWeek
            findViewById<RecyclerView>(R.id.list_hour).layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

//        viewModel.apply {
//            getLiveData().observe(viewLifecycleOwner) { displayWeather(it) }
//            getServerWeather(weatherBundle.city.lat, weatherBundle.city.lon)
//        }
        getWeather()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun displayWeather(selectState: SelectState) {
        with(binding)
        {
            when (selectState) {
                is SelectState.Loading -> {
                    mainView.hide()
                    loadingLayout.show()
                }
                is SelectState.Success -> {
                    mainView.show()
                    loadingLayout.hide()
                    selectState.weatherDTO.fact?.apply {
                        cityName.text = weatherBundle.city.cityName
                        feelsLikeText.text = feels_like.toString().addDegree()
                        conditionText.text = condition
                        weatherText.text = temp.toString().addDegree()
                        dataText.text = Date().formatDate()
                    }
                    adapterHour.setWeather(selectState.weatherDTO.forecasts[0].hours)
                    adapterWeek.setWeather(selectState.weatherDTO.forecasts)
                }
                is SelectState.Error -> {
                    mainFrame.hide()
                    loadingLayout.show()
                    mainView.showSnackBarError(
                        selectState.message,
                        resources.getString(R.string.reload),
                        {
                            viewModel.getServerWeather(
                                weatherBundle.city.lat,
                                weatherBundle.city.lon
                            )
                        })
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_item_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                loadResultsReceiver,
                IntentFilter(BROADCAST_OBSERVER)
            )
            it.registerReceiver(
                loadResultsReceiver,
                IntentFilter(CONNECTIVITY_ACTION)
            )
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

    private fun getWeather() {
        binding.mainView.visibility = View.GONE
        binding.loadingLayout.visibility = View.VISIBLE
        context?.let {
            it.startService(Intent(it, SelectService::class.java).apply {
                putExtra(
                    LATITUDE_EXTRA,
                    weatherBundle.city.lat
                )
                putExtra(
                    LONGITUDE_EXTRA,
                    weatherBundle.city.lon
                )
            })
        }
    }

    private val loadResultsReceiver: BroadcastReceiver = object :
        BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.N)
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action.toString() == CONNECTIVITY_ACTION) {
                Toast.makeText(requireContext(), "Message", Toast.LENGTH_LONG).show()
            }
            with(binding.mainFrame)
            {
                when (intent.getStringExtra(DETAILS_LOAD_RESULT_EXTRA)) {
                    DETAILS_INTENT_EMPTY_EXTRA -> showSnackBarError(
                        DETAILS_INTENT_EMPTY_EXTRA,
                        "",
                        {})
                    DETAILS_DATA_EMPTY_EXTRA -> showSnackBarError(DETAILS_DATA_EMPTY_EXTRA, "", {})
                    DETAILS_RESPONSE_EMPTY_EXTRA -> showSnackBarError(
                        DETAILS_RESPONSE_EMPTY_EXTRA,
                        "",
                        {})
                    DETAILS_REQUEST_ERROR_EXTRA -> showSnackBarError(
                        DETAILS_REQUEST_ERROR_EXTRA,
                        "",
                        {})
                    DETAILS_REQUEST_ERROR_MESSAGE_EXTRA -> showSnackBarError(
                        DETAILS_REQUEST_ERROR_MESSAGE_EXTRA,
                        "",
                        {})
                    DETAILS_URL_MALFORMED_EXTRA -> showSnackBarError(
                        DETAILS_URL_MALFORMED_EXTRA,
                        "",
                        {})
                    DETAILS_RESPONSE_SUCCESS_EXTRA -> {
                        intent.getParcelableExtra<WeatherDTO>(KEY_WEATHER_DTO)?.let {
                            displayWeather(SelectState.Success(it))
                        } ?: run {
                            showSnackBarError("EMPTY weather", "", {})
                        }
                    }
                }
            }
        }
    }
}