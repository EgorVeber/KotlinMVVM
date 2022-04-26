package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentCitysBinding
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.hide
import ru.gb.veber.kotlinmvvm.model.show
import ru.gb.veber.kotlinmvvm.model.showSnackBar
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view.adapter.OnCityClickListener
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer

class CitysFragment : Fragment(), OnCityClickListener {

    private var _binding: FragmentCitysBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelWeather by lazy {
        ViewModelProvider(this).get(ViewModelWeather::class.java)
    }
    private val adapter = CitysFragmentAdapter()
    private var isDataSetRus: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        adapter.setOnCityClickListener(this)

        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
            getWeatherFromLocalSourceRus()
        }

        binding.apply {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        }
    }

    override fun onCityClick(weather: Weather) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .add(R.id.fragment_container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.KEY_WEATHER, weather)
                })).addToBackStack("").commitAllowingStateLoss()
        }
    }

    private fun renderData(appState: AppState?) {
        with(binding)
        {
            when (appState) {
                is AppState.Success -> {
                    mainFragmentLoadingLayout.hide()
                    adapter.setWeather(appState.weatherList)
                }
                is AppState.Loading -> mainFragmentLoadingLayout.show()
                is AppState.Error -> {
                    mainFragmentLoadingLayout.hide()
                    mainFragmentRecyclerView.showSnackBar(
                        R.string.error,
                        R.string.reload,
                        { viewModel.getWeatherFromLocalSourceRus() })
                }
                else -> mainFragmentLoadingLayout.hide()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_item_update) changeWeatherDataSet()
        return super.onOptionsItemSelected(item)
    }

    private fun changeWeatherDataSet() =
        viewModel.apply {
            binding.apply {
                if (isDataSetRus) {
                    getWeatherFromLocalSourceWorld()
                    mainFragmentFAB.setImageResource(R.drawable.ic_earth)
                } else {
                    getWeatherFromLocalSourceRus()
                    mainFragmentFAB.setImageResource(R.drawable.ic_russia)
                }
            }
        }.also { isDataSetRus = !isDataSetRus }
}