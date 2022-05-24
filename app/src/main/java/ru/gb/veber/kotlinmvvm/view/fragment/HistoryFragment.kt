package ru.gb.kotlinapp.view.history

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentHistoryBinding
import ru.gb.veber.kotlinmvvm.model.City
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.model.showSnackBarError
import ru.gb.veber.kotlinmvvm.view.fragment.DetailsFragment
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.SelectState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelHistory
import ru.gb.veber.kotlinmvvm.view_model.ViewModelWeatherServer


class HistoryFragment : Fragment(), ClickHistory {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ViewModelHistory by lazy {
        ViewModelProvider(this).get(ViewModelHistory::class.java)
    }

    private val adapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        adapter.setOnNoteCliclListner(this)
        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })

    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllHistory()
    }

    override fun selectWeather(weather: Weather) {
        activity?.supportFragmentManager?.let {
            it.beginTransaction()
                .replace(R.id.fragment_container, DetailsFragment.newInstance(Bundle().apply {
                    putParcelable(DetailsFragment.KEY_WEATHER, weather)
                })).addToBackStack("").commitAllowingStateLoss()
        }
    }

    override fun deleteHistory() {
        viewModel.deleteHistory()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                }
                if (appState.weatherList.isEmpty()) {
                    Toast.makeText(context, "Empty", Toast.LENGTH_LONG).show()
                }
                adapter.setData(appState.weatherList)
            }
            is AppState.Loading -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.GONE
                    loadingLayout.visibility = View.VISIBLE
                }
            }
            is AppState.Error -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
                    historyFragmentRecyclerview.showSnackBarError(getString(R.string.error),
                        getString(R.string.reload),
                        {
                            viewModel.getAllHistory()
                        })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            HistoryFragment()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_history_toolbar, menu)
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_item_search).isVisible = false
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.deleteHistory()
        return super.onOptionsItemSelected(item)
    }
}