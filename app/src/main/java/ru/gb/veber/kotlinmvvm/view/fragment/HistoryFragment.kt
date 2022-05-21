package ru.gb.kotlinapp.view.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentHistoryBinding
import ru.gb.veber.kotlinmvvm.model.showSnackBarError
import ru.gb.veber.kotlinmvvm.view_model.AppState
import ru.gb.veber.kotlinmvvm.view_model.ViewModelHistory

class HistoryFragment : Fragment() {
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

        binding.historyFragmentRecyclerview.adapter = adapter
        viewModel.historyLiveData.observe(viewLifecycleOwner, Observer { renderData(it) })
        viewModel.getAllHistory()
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Success -> {
                with(binding) {
                    historyFragmentRecyclerview.visibility = View.VISIBLE
                    loadingLayout.visibility = View.GONE
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
}