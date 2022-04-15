package ru.gb.veber.kotlinmvvm.view

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentMainBinding
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather
import ru.gb.veber.kotlinmvvm.weatherData
import java.text.SimpleDateFormat

class MainFragment:Fragment(){

    private var _binding:FragmentMainBinding? = null
    private val binding get() =_binding!!
    private lateinit var viewModel:ViewModelWeather

    var adapterHour = AdapterHour()
    var adapterWeek = AdapterWeek()
    companion object
    {
        fun newInstance()=MainFragment()
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding= FragmentMainBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        var recyclerDay = view.findViewById<RecyclerView>(R.id.list_hour)
        recyclerDay.adapter=adapterHour
        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerDay.layoutManager=layoutManager

        var recyclerWeek = view.findViewById<RecyclerView>(R.id.list_week)
        recyclerWeek.adapter=adapterWeek
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel =ViewModelProvider(this).get(ViewModelWeather::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it)})
        viewModel.getWeather()

    }
    private fun renderData(appState: AppState)
    {
        when(appState)
        {
            is AppState.Error->
            {
                binding.progressBarDay.visibility=View.GONE
                binding.progressBarWeek.visibility=View.GONE
                binding.progressBarDescription.visibility=View.GONE
                Snackbar.make(binding.root,"ERROR",Snackbar.LENGTH_INDEFINITE).
                setAction("Reload")
                {
                    viewModel.getWeather()
                }.show()
            }
            is AppState.Loading->
            {
                binding.linerDay.visibility=View.GONE
                binding.linerDescription.visibility=View.GONE
                binding.linerWeek.visibility=View.GONE
                binding.progressBarDay.visibility=View.VISIBLE
                binding.progressBarWeek.visibility=View.VISIBLE
                binding.progressBarDescription.visibility=View.VISIBLE
            }
            is AppState.Success->
            {
                val weatherData = appState.weatherData
                binding.progressBarDay.visibility=View.GONE
                binding.progressBarWeek.visibility=View.GONE
                binding.progressBarDescription.visibility=View.GONE
                binding.linerDay.visibility=View.VISIBLE
                binding.linerDescription.visibility=View.VISIBLE
                binding.linerWeek.visibility=View.VISIBLE
                setData(weatherData)
            }
        }
    }
    private fun setData(weatherData:Weather)
    {
        adapterHour.setWeather(ru.gb.veber.kotlinmvvm.weatherData)
        adapterWeek.setWeather(ru.gb.veber.kotlinmvvm.weatherWeek)
        binding.cityName.text= weatherData.city.cityName
        binding.dataText.text= SimpleDateFormat(getString(R.string.time_format)).format(weatherData.time)
        binding.weatherIcon.background=resources.getDrawable(R.drawable.sun264)
        binding.weatherText.text= weatherData.temperature.toString()
        binding.conditionText.text=weatherData.condition
        binding.feelsLikeText.text=resources.getString(R.string.feelsLike)+" "+weatherData.feelsLike.toString()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_item_update) viewModel.getWeather()
        return super.onOptionsItemSelected(item)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}