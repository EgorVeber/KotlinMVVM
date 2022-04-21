package ru.gb.veber.kotlinmvvm.view

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Weather
import java.text.SimpleDateFormat
import java.util.*

class AdapterWeek:RecyclerView.Adapter<HolderWeek>(){

    private var weatherData: List<Weather> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderWeek {
        return HolderWeek(LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false) as View)
    }
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: HolderWeek, position: Int) = holder.bind(weatherData[position])
    override fun getItemCount() = weatherData.size
}
class HolderWeek(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var weather = Weather()
    private val weekDay: TextView = itemView.findViewById(R.id.week_day)
    private val weekWeatherDay: TextView = itemView.findViewById(R.id.weather_week_day)
    private val weekWeatherNight: TextView = itemView.findViewById(R.id.weather_week_night)
    fun bind(weather: Weather) {
        this.weather = weather
        weekDay.text=weather.forecastDate
        weekWeatherDay.text=weather.tempMin.toString()+"/"
        weekWeatherNight.text=weather.tempMax.toString()
    }
}
