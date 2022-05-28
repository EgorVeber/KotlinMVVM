package ru.gb.veber.kotlinmvvm.view.fragment

import android.content.Context
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
import ru.gb.veber.kotlinmvvm.model.showSnackBarResources
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view.adapter.OnCityClickListener
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather

class FragmentCitys : Fragment(), OnCityClickListener {

    private var _binding: FragmentCitysBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelWeather by lazy {
        ViewModelProvider(this).get(ViewModelWeather::class.java)
    }
    private val adapter = CitysFragmentAdapter()
    private var isDataSetRus: Boolean = true

    companion object {
        const val KEY_CITS = "KEY_CITS"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCitysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        adapter.setOnCityClickListener(this)

        activity?.let {
            isDataSetRus = it.getPreferences(Context.MODE_PRIVATE).getBoolean(KEY_CITS, true)
        }

        viewModel.apply {
            getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
            if (isDataSetRus) {
                getWeatherFromLocalSourceRus()
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
            } else {
                binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
                getWeatherFromLocalSourceWorld()
            }
        }

        binding.apply {
            mainFragmentRecyclerView.adapter = adapter
            mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }
        }
    }

    override fun onCityClick(weather: Weather) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .add(R.id.fragment_container, FragmentDetails.newInstance(Bundle().apply {
                    putParcelable(FragmentDetails.KEY_WEATHER, weather)
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
                    mainFragmentRecyclerView.showSnackBarResources(
                        R.string.error,
                        R.string.reload,
                        { viewModel.getWeatherFromLocalSourceRus() })
                }
                else -> mainFragmentLoadingLayout.hide()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.findItem(R.id.menu_item_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.menu_item_update -> changeWeatherDataSet()
        }
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
        }.also {
            isDataSetRus = !isDataSetRus
            activity?.let {
                it.getPreferences(Context.MODE_PRIVATE).edit().putBoolean(KEY_CITS, isDataSetRus)
                    .apply()
            }
        }
}