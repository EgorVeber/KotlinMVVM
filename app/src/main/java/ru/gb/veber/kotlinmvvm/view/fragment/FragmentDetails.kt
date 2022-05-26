package ru.gb.veber.kotlinmvvm.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_details.*
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentDetailsBinding
import ru.gb.veber.kotlinmvvm.model.*
import ru.gb.veber.kotlinmvvm.view.MainActivity
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelDialog
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer
import java.util.*


class FragmentDetails : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val adapterHour = AdapterHour()
    private val adapterWeek = AdapterWeek()
    private lateinit var weatherBundle: Weather
    private var weatherInfo: Info? = null

    private val viewModel: ViewModelWeatherServer by lazy {
        ViewModelProvider(requireActivity()).get(ViewModelWeatherServer::class.java)
    }

    private val viewModelDialog: ViewModelDialog by lazy {
        ViewModelProvider(requireActivity()).get(ViewModelDialog::class.java)
    }

    companion object {
        const val KEY_WEATHER = "KEY_WEATHER"
        fun newInstance(bundle: Bundle) = FragmentDetails().apply { arguments = bundle }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("CutPasteId")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        weatherBundle = arguments?.getParcelable(KEY_WEATHER) ?: Weather()

        view.apply {
            findViewById<RecyclerView>(R.id.list_hour).apply {
                adapter = adapterHour
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            }
            findViewById<RecyclerView>(R.id.list_week).adapter = adapterWeek
        }

        viewModel.apply {
            detailsLiveData.observe(viewLifecycleOwner) { renderData(it) }
            getWeatherFromRemoteSource(weatherBundle.city.lat, weatherBundle.city.lon)
        }

        info_icon.setOnClickListener { infoClick() }
    }

    private fun infoClick() {
        weatherInfo?.let {
            viewModelDialog.setWeatherData(it)
        }
        FragmentDialog().show(requireActivity().supportFragmentManager, null)
    }

    private fun renderData(selectState: SelectState) {
        when (selectState) {
            is SelectState.Success -> {
                binding.mainView.visibility = View.VISIBLE
                binding.loadingLayout.visibility = View.GONE
                setWeather(selectState.weatherDTO)
            }
            is SelectState.Error -> {
                //binding.mainView.visibility = View.VISIBLE
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

    private fun setWeather(weatherDTO: WeatherDTO) {
        weatherInfo = weatherDTO.info

        activity?.let {
            if (it.getSharedPreferences(FragmentSettings.FILE_SETTINGS, Context.MODE_PRIVATE)
                    .getBoolean(FragmentSettings.KEY_HISTORY, false)
            ) {
                saveCity(weatherBundle.city, factToWeather(weatherDTO.fact!!))
            }
        }

        with(binding)
        {
            mainView.show()
            loadingLayout.hide()
            weatherDTO.fact!!.apply {
                cityName.text = weatherBundle.city.cityName
                feelsLikeText.text = feels_like.toString().addDegree()
                conditionText.text = condition
                weatherText.text = temp.toString().addDegree()
                dataText.text = Date().formatDate()
                weatherIcon.loadSvg(icon!!)
                windSpeed.text = "$wind_speed м/с"
                PressureMm.text = "$pressure_mm мм рт"
                Humidity.text = "$humidity%"
                Season.text = season
            }
            weatherDTO.forecasts[0].apply {
                binding.sunrise.text = sunrise
                binding.sunset.text = sunset
            }
            adapterHour.setWeather(weatherDTO.forecasts[0].hours)
            adapterWeek.setWeather(weatherDTO.forecasts)
        }
    }

    private fun saveCity(city: City, weather: Weather) {
        with(weather)
        {
            viewModel.saveCityToDB(Weather(city, temperature, feelsLike, condition))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_item_search).isVisible = false
        menu.findItem(R.id.menu_content_provider).isVisible = false
        if (menu.findItem(R.id.menu_item_delete) != null) {
            menu.findItem(R.id.menu_item_delete).isVisible = false
        }
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                activity?.supportFragmentManager?.popBackStack()
                (activity as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}