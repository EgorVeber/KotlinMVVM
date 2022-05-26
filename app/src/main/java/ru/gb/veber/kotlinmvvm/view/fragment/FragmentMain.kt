package ru.gb.veber.kotlinmvvm.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import ru.gb.kotlinapp.view.history.HistoryFragment
import ru.gb.veber.kotlinmvvm.databinding.FragmentMainBinding
import ru.gb.veber.kotlinmvvm.view.adapter.vpAdapter


class FragmentMain : Fragment() {
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val listFragment = listOf(
        CitysFragment(),
        HistoryFragment(),
        SettingsFragment()
    )
    private val listTitle = listOf("Cits", "History", "Settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.placeholder.adapter = vpAdapter(requireActivity(), listFragment)
        TabLayoutMediator(binding.tabLayout, binding.placeholder) { tab, pos ->
            tab.text = listTitle[pos]
        }.attach()
    }
}