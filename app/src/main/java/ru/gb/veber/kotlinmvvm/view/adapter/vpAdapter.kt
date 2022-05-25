package ru.gb.veber.kotlinmvvm.view.adapter

import android.util.Log
import androidx.fragment.app.*
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder
import ru.gb.kotlinapp.view.history.HistoryFragment
import ru.gb.veber.kotlinmvvm.view.fragment.CitysFragment
import ru.gb.veber.kotlinmvvm.view.fragment.SettingsFragment


class vpAdapter(fa: FragmentActivity, private val list: List<Fragment>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}
