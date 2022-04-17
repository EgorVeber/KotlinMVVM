package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Weather
import ru.gb.veber.kotlinmvvm.view.fragment.CitysFragment

class CitysFragmentAdapter(private var onItemViewClickListener: CitysFragment.OnItemViewClickListener?)
    :RecyclerView.Adapter<CitysFragmentAdapter.MainViewHolder>(){

    private var weatherData:List<Weather> = listOf()

    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }
    fun removeListener() {
        onItemViewClickListener = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            LayoutInflater.from(parent.context).
            inflate(R.layout.fragment_main_recycler_item,parent,false) as View)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(weatherData[position])
    }

    override fun getItemCount(): Int {
        return weatherData.size
    }

    inner class MainViewHolder(view: View):RecyclerView.ViewHolder(view)
    {
        fun bind(weather: Weather)
        {
            itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.cityName
            itemView.setOnClickListener {
                onItemViewClickListener?.onItemViewClick(weather)
            }
        }
    }
}