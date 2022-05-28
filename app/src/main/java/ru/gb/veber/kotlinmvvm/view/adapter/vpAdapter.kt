package ru.gb.veber.kotlinmvvm.view.adapter

import androidx.fragment.app.*
import androidx.viewpager2.adapter.FragmentStateAdapter


class vpAdapter(fa: FragmentActivity, private val list: List<Fragment>) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}
