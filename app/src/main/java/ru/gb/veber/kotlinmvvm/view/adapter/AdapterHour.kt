package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.*
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Weather
import java.text.SimpleDateFormat

class AdapterHour:RecyclerView.Adapter<HolderHour>(){

    private var weatherData: List<Weather> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HolderHour {
        return HolderHour(LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false) as View)
    }
    fun setWeather(data: List<Weather>) {
        weatherData = data
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: HolderHour, position: Int) = holder.bind(weatherData[position])
    override fun getItemCount() = weatherData.size
}
class HolderHour(itemView: View) : RecyclerView.ViewHolder(itemView){
    private var weather = Weather()
    private val hourTime: TextView = itemView.findViewById(R.id.hour_time)
    private val hourIcon: ImageView = itemView.findViewById(R.id.hour_icon)
    private val hourWeather: TextView = itemView.findViewById(R.id.hour_weather)
    fun bind(weather: Weather) {
        this.weather = weather
        hourTime.text=SimpleDateFormat("H:m").format(weather.time)
        //hourIcon.background=
        hourWeather.text=weather.temperature.toString()
    }
}
