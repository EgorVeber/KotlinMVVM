package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.ForecastsCustom
import ru.gb.veber.kotlinmvvm.model.formatWeek

class AdapterWeek : RecyclerView.Adapter<HolderWeek>() {

    private var forecastsData: List<ForecastsCustom> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HolderWeek(
        LayoutInflater.from(parent.context).inflate(R.layout.week_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: HolderWeek, position: Int) =
        holder.bind(forecastsData[position])

    override fun getItemCount() = forecastsData.size

    fun setWeather(forecastsData: List<ForecastsCustom>) {
        this.forecastsData = forecastsData
        notifyDataSetChanged()
    }
}

class HolderWeek(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(forecasts: ForecastsCustom) {
        with(itemView)
        {
            forecasts.apply {
                findViewById<TextView>(R.id.week_day).text = date.formatWeek()
                findViewById<TextView>(R.id.weather_week_max).text = temp_max.toString() + "/"
                findViewById<TextView>(R.id.weather_week_min).text = temp_min.toString()
            }
        }
    }
}
