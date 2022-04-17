package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentDetailsBinding
import ru.gb.veber.kotlinmvvm.model.Hours
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterHour
import ru.gb.veber.kotlinmvvm.view.adapter.AdapterWeek
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather
import java.text.SimpleDateFormat

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ViewModelWeather

    private val adapterHour = AdapterHour()
    private val  adapterWeek = AdapterWeek()

    companion object {
        const val KEY_WEATHER = "KEY_WEATHER"
        fun newInstance(bundle: Bundle): DetailsFragment {
            var fragment = DetailsFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar!!.subtitle = resources.getString(R.string.city)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        var recyclerDay = view.findViewById<RecyclerView>(R.id.list_hour)
        var recyclerWeek = view.findViewById<RecyclerView>(R.id.list_week)

        recyclerDay.adapter=adapterHour
        recyclerDay.layoutManager=layoutManager
        recyclerWeek.adapter=adapterWeek




        val weatherData = arguments?.getParcelable<Weather>(KEY_WEATHER)
        if (weatherData != null) {
            binding.cityName.text = weatherData.city.cityName
            binding.dataText.text = SimpleDateFormat(getString(R.string.time_format)).format(weatherData.time)
            binding.weatherIcon.background = resources.getDrawable(R.drawable.sun264)
            binding.weatherText.text = weatherData.temperature.toString()
            binding.conditionText.text = weatherData.condition
            binding.feelsLikeText.text = resources.getString(R.string.feelsLike) + " " + weatherData.feelsLike.toString()
            adapterHour.setWeather(  weatherData.forecastList[0].hours)
             adapterWeek.setWeather(weatherData.forecastList)
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