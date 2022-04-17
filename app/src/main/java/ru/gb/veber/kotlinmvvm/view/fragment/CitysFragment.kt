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
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view.adapter.OnCityClickListener
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather

class CitysFragment : Fragment(), OnCityClickListener {

    private var _binding: FragmentCitysBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: ViewModelWeather
    private val adapter = CitysFragmentAdapter()
    private var isDataSetRus: Boolean = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCitysBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        binding.mainFragmentRecyclerView.adapter = adapter

        adapter.setOnCityClickListener(this)
        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        viewModel = ViewModelProvider(this).get(ViewModelWeather::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getWeatherFromLocalSourceRus()
    }
    override fun onCityClick(weather: Weather) {
        val fragmentManager = activity?.supportFragmentManager
        if (fragmentManager != null) {
            val bundle = Bundle()
            bundle.putParcelable(DetailsFragment.KEY_WEATHER,weather)
            fragmentManager.beginTransaction().add(R.id.fragment_container,DetailsFragment.newInstance(bundle)).
            addToBackStack("").commitAllowingStateLoss()
        }
    }
    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherList)
            }
            is AppState.Loading -> binding.mainFragmentLoadingLayout.visibility = View.VISIBLE

            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar.make(binding.mainFragmentFAB, getString(R.string.error), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) {
                        viewModel.getWeatherFromLocalSourceRus()
                    }
                    .show()
            }
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.menu_item_update) changeWeatherDataSet()
        return super.onOptionsItemSelected(item)
    }
    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_earth)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_russia)
        }
        isDataSetRus = !isDataSetRus
    }
}