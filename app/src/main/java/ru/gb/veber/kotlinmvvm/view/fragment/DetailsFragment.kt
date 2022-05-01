package ru.gb.veber.kotlinmvvm.view.fragment

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentDetailsBinding
import ru.gb.veber.kotlinmvvm.model.*
import ru.gb.veber.kotlinmvvm.view.*
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import java.util.*

const val BROADCAST_OBSERVER = "BROADCAST_OBSERVER"
const val KEY_WEATHER_DTO = "KEY_WEATHER_DTO"

class DetailsFragment : Fragment(), LoadReceiver {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapterHour = AdapterHour()
    private val adapterWeek = AdapterWeek()
    private lateinit var weatherBundle: Weather

    private val loadResultsReceiver = LoadResultsReceiver(this)

    companion object {
        const val KEY_WEATHER = "KEY_WEATHER"
        fun newInstance(bundle: Bundle) = DetailsFragment().apply { arguments = bundle }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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

        weatherBundle = arguments?.getParcelable(KEY_WEATHER) ?: Weather()

        view.apply {
            findViewById<RecyclerView>(R.id.list_hour).adapter = adapterHour
            findViewById<RecyclerView>(R.id.list_week).adapter = adapterWeek
            findViewById<RecyclerView>(R.id.list_hour).layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        startService()
    }

    private fun startService() {
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

    @RequiresApi(Build.VERSION_CODES.N)
    override fun displayLoadReceiverWeather(weatherDTO: WeatherDTO) {
        Log.d("TAG", "displayLoadReceiverWeather() called with: weatherDTO = ${weatherDTO}")
        with(binding)
        {
            mainView.show()
            loadingLayout.hide()
            weatherDTO.fact?.apply {
                cityName.text = weatherBundle.city.cityName
                feelsLikeText.text = feels_like.toString().addDegree()
                conditionText.text = condition
                weatherText.text = temp.toString().addDegree()
                dataText.text = Date().formatDate()
            }
            adapterHour.setWeather(weatherDTO.forecasts[0].hours)
            adapterWeek.setWeather(weatherDTO.forecasts)
        }
    }

    override fun displayLoadReceiverError(string: String) {
        binding.apply {
            mainView.showSnackBarError(
                string,
                resources.getString(R.string.reload),
                { startService() })
            mainView.hide()
            loadingLayout.hide()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(
                loadResultsReceiver, IntentFilter(BROADCAST_OBSERVER)
            )
        }
    }

    override fun onDestroy() {
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(loadResultsReceiver)
        }
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_item_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
}