package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentDetailsBinding
import ru.gb.veber.kotlinmvvm.model.*
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer
import java.util.*

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

        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner) { displayWeather(it) }
            getServerWeather(weatherBundle.city.lat, weatherBundle.city.lon)
        }
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
}