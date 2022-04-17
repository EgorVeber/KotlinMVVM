package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Weather

class CitysFragmentAdapter() :RecyclerView.Adapter<CitysFragmentHolder>(){

    private var weatherData:List<Weather> = listOf()
    private lateinit var cityClick:OnCityClickListener

    fun setOnCityClickListener(listener: OnCityClickListener) {
        cityClick = listener
    }
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitysFragmentHolder {
        return CitysFragmentHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_citys_item,parent,false) as View,cityClick)
    }
    override fun onBindViewHolder(holder: CitysFragmentHolder, position: Int) {
        holder.bind(weatherData[position])
    }
    override fun getItemCount(): Int {
        return weatherData.size
    }
}
class CitysFragmentHolder(view: View, cityClick: OnCityClickListener):RecyclerView.ViewHolder(view)
{
    private  var cityClick:OnCityClickListener = cityClick
    fun bind(weather: Weather)
    {
        itemView.findViewById<TextView>(R.id.mainFragmentRecyclerItemTextView).text = weather.city.cityName
        itemView.setOnClickListener {
            cityClick.onCityClick(weather)
        }
    }
}
interface OnCityClickListener {
    fun onCityClick(weather: Weather)
}
