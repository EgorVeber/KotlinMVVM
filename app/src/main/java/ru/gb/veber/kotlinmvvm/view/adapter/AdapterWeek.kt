package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.*

class AdapterWeek : RecyclerView.Adapter<HolderWeek>() {

    private var forecastsData: List<Forecasts> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HolderWeek(
        LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: HolderWeek, position: Int) =
        holder.bind(forecastsData[position])

    override fun getItemCount() = forecastsData.size

    fun setWeather(forecastsData: List<Forecasts>) {
        this.forecastsData = forecastsData
        notifyDataSetChanged()
    }
}

class HolderWeek(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(forecasts: Forecasts) {
        with(itemView)
        {
            findViewById<TextView>(R.id.week_day).text = dayWeekFromString(forecasts.date).formatWeek()
            forecasts.parts.apply {
                findViewById<TextView>(R.id.night_temperature).text =
                    getSlash(night.temp_min, night.temp_max)
                findViewById<TextView>(R.id.morning_temperature).text =
                    getSlash(morning.temp_min, morning.temp_max)
                findViewById<TextView>(R.id.day_temperature).text =
                    getSlash(day.temp_min, day.temp_max)
                findViewById<TextView>(R.id.evening_temperature).text =
                    getSlash(evening.temp_min, evening.temp_max)
                findViewById<AppCompatImageView>(R.id.night_icon).loadSvg(night.icon!!)
                findViewById<AppCompatImageView>(R.id.morning_icon).loadSvg(morning.icon!!)
                findViewById<AppCompatImageView>(R.id.day_icon).loadSvg(day.icon!!)
                findViewById<AppCompatImageView>(R.id.evening_icon).loadSvg(evening.icon!!)
            }
        }
    }
}
