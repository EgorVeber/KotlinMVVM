package ru.gb.veber.kotlinmvvm.view.adapter

import android.provider.Settings.Global.getString
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Forecasts
import java.text.SimpleDateFormat

class AdapterWeek:RecyclerView.Adapter<HolderWeek>(){

    private var forecastsData: List<Forecasts> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderWeek {
        return HolderWeek(LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false) as View)
    }
    fun setWeather(forecastsData: List<Forecasts>) {
        this.forecastsData = forecastsData
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: HolderWeek, position: Int) = holder.bind(forecastsData[position])
    override fun getItemCount() = forecastsData.size
}
class HolderWeek(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var forecasts = Forecasts()
    private val weekDay: TextView = itemView.findViewById(R.id.week_day)
    private val weekWeatherMax: TextView = itemView.findViewById(R.id.weather_week_max)
    private val weekWeatherMin: TextView = itemView.findViewById(R.id.weather_week_min)
    fun bind(forecasts: Forecasts) {
        this.forecasts = forecasts
        weekDay.text= SimpleDateFormat("EEEE").format(forecasts.date)
        weekWeatherMax.text=forecasts.temp_max.toString()+"/"
        weekWeatherMin.text=forecasts.temp_min.toString()
    }
}
