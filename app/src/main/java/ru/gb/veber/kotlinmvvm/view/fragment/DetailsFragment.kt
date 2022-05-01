package ru.gb.veber.kotlinmvvm.view.fragment

import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
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
import ru.gb.veber.kotlinmvvm.view.*
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer
import java.util.*
import kotlin.math.log


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

        viewModel.detailsLiveData.observe(viewLifecycleOwner) { renderData(it) }
        viewModel.getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
    }

    private fun renderData(selectState: SelectState) {
        when (selectState) {
            is SelectState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setViewWeather(selectState.weatherDTO)
            }
            is SelectState.Error -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                binding.mainView.showSnackBarError(
                    getString(R.string.error),
                    getString(R.string.reload),
                    {
                        viewModel.getWeatherFromRemoteSource(
                            weatherBundle.city.lat,
                            weatherBundle.city.lon
                        )
                    })
            }
            is SelectState.Loading -> {
                binding.mainView.visibility = View.GONE
                binding.loadingLayout.visibility = View.VISIBLE
            }
        }
    }

    fun setViewWeather(weatherDTO: WeatherDTO) {
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