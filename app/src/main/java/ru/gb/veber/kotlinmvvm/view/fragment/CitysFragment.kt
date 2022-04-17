package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentCitysBinding
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.view.adapter.CitysFragmentAdapter
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeather

class CitysFragment : Fragment() {

    private var _binding: FragmentCitysBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel:ViewModelWeather

    private val adapter = CitysFragmentAdapter(object: OnItemViewClickListener {
        override fun onItemViewClick(weather: Weather) {
            val manager = activity?.supportFragmentManager
            if(manager!=null)
            {
                val bundle = Bundle()
                bundle.putParcelable(DetailsFragment.BUNDLE_EXTRA,weather)
                manager.beginTransaction().replace(R.id.fragment_container,DetailsFragment.newInstance(bundle)).
                addToBackStack("").commitAllowingStateLoss()
            }
        }
    })

    private var isDataSetRus:Boolean = true
    companion object {
        fun newInstance() =
            CitysFragment()
    }
    interface OnItemViewClickListener {
        fun onItemViewClick(weather: Weather)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentCitysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.mainFragmentRecyclerView.adapter = adapter

        binding.mainFragmentFAB.setOnClickListener { changeWeatherDataSet() }

        viewModel = ViewModelProvider(this).get(ViewModelWeather::class.java)
        viewModel.getLiveData().observe(viewLifecycleOwner, Observer { renderData(it) })

        viewModel.getWeatherFromLocalSourceRus()
    }

    private fun renderData(appState: AppState?) {
        when (appState) {
            is AppState.Success -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                adapter.setWeather(appState.weatherList)
            }
            is AppState.Loading -> {
                binding.mainFragmentLoadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.mainFragmentLoadingLayout.visibility = View.GONE
                Snackbar
                    .make(binding.mainFragmentFAB, getString(R.string.error),
                        Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.reload)) {
                        viewModel.getWeatherFromLocalSourceRus() }
                    .show()
            }
        }
    }
    private fun changeWeatherDataSet() {
        if (isDataSetRus) {
            viewModel.getWeatherFromLocalSourceWorld()
            binding.mainFragmentFAB.setImageResource(R.drawable.ic_launcher_background)
        } else {
            viewModel.getWeatherFromLocalSourceRus()
            binding.mainFragmentFAB.setImageResource(R.drawable.clouds3)
        }
        isDataSetRus = !isDataSetRus
    }
    override fun onDestroy() {
        adapter.removeListener()
        super.onDestroy()
    }
}