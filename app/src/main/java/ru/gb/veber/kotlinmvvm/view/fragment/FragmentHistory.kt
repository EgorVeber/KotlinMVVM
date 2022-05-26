package ru.gb.kotlinapp.view.history

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentHistoryBinding
import ru.gb.veber.kotlinmvvm.model.*
import ru.gb.veber.kotlinmvvm.view.fragment.FragmentDetails
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelHistory


class FragmentHistory : Fragment(), ClickHistory {

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
                .replace(R.id.fragment_container, FragmentDetails.newInstance(Bundle().apply {
                    putParcelable(FragmentDetails.KEY_WEATHER, weather)
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
                    binding.emptyView.hide()
                }
                if (appState.weatherList.isEmpty()) {
                    binding.historyFragmentRecyclerview.hide()
                    binding.emptyView.show()
                }
                adapter.setData(appState.weatherList)
            }
            is AppState.SuccessHistory -> {
                adapter.setData(convertHistoryEntityToWeather(appState.weatherList))
            }
            is AppState.Loading -> {
                with(binding) {
                    historyFragmentRecyclerview.hide()
                    loadingLayout.show()
                }
            }
            is AppState.Error -> {
                Log.d("TAG", "renderData() called with: appState = $appState")
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
            FragmentHistory()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        var searchItem: MenuItem? = null
        var searchView: SearchView? = null


        inflater.inflate(R.menu.menu_history_toolbar, menu)
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_content_provider).isVisible = false


        searchItem = menu.findItem(R.id.menu_item_search)
        searchView = searchItem.getActionView() as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                viewModel.filterHistory("%$query%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.filterHistory("%$newText%")
                return false
            }
        })


        MenuItemCompat.setOnActionExpandListener(
            searchItem,
            object : MenuItemCompat.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    //  viewModel.getAllHistory()
                    return true
                }
            })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_item_delete ->
                viewModel.deleteHistory()
        }
        return super.onOptionsItemSelected(item)
    }
}
