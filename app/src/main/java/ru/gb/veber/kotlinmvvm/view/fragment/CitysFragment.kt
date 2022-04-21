package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentCitysBinding
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.hide
import ru.gb.veber.kotlinmvvm.model.showSnackBar
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view.adapter.OnCityClickListener
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather

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
        binding.mainFragmentRecyclerView.adapter = adapter

        adapter.setOnCityClickListener(this)
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
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
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.hide()
                adapter.setWeather(appState.weatherList)
            }
            is AppState.Loading -> binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            is AppState.Error -> binding.mainFragmentLoadingLayout.visibility = View.GONE
        }
        binding.mainFragmentLoadingLayout.showSnackBar(
            R.string.error,
            R.string.reload,
            { viewModel.getWeatherFromLocalSourceRus() })
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