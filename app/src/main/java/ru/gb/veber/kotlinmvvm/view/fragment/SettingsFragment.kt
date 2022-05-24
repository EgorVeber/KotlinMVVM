package ru.gb.veber.kotlinmvvm.view.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.gb.kotlinapp.view.history.HistoryFragment
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.databinding.FragmentMainBinding
import ru.gb.veber.kotlinmvvm.databinding.FragmentSettingsBinding
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.view.adapter.vpAdapter
import kotlin.math.log


class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var saveHistory: Boolean = false

    companion object {
        const val FILE_SETTINGS = "FILE_SETTINGS"
        const val KEY_HISTORY = "KEY_HISTORY"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.checkboxSettings.setOnClickListener(saveHistoryCheckBox)
    }

    var saveHistoryCheckBox = View.OnClickListener {
        saveHistory = binding.checkboxSettings.isChecked
        activity?.let {
            it.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE).edit()
                .putBoolean(KEY_HISTORY, saveHistory).apply()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.let {
            saveHistory = it.getSharedPreferences(FILE_SETTINGS, Context.MODE_PRIVATE)
                .getBoolean(KEY_HISTORY, false)
        }
        binding.checkboxSettings.isChecked = saveHistory
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.findItem(R.id.menu_item_update).isVisible = false
        menu.findItem(R.id.menu_item_search).isVisible = false
    }
}