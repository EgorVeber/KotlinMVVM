package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.*
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Hours
import ru.gb.veber.kotlinmvvm.model.addDegree
import ru.gb.veber.kotlinmvvm.model.*

class AdapterHour : RecyclerView.Adapter<HolderHour>() {

    private var hoursData: List<Hours> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HolderHour(
        LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false) as View
    )

    override fun onBindViewHolder(holder: HolderHour, position: Int) =
        holder.bind(hoursData[position])

    override fun getItemCount() = hoursData.size

    fun setWeather(hoursData: List<Hours>) {
        this.hoursData = hoursData
        notifyDataSetChanged()
    }
}

class HolderHour(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(hours: Hours) {
        with(itemView)
        {
            findViewById<TextView>(R.id.hour_time).text = hours.hour + ":00"
            findViewById<AppCompatImageView>(R.id.hour_icon).loadSvg(hours.icon!!)
            findViewById<TextView>(R.id.hour_weather).text = hours.temp.toString().addDegree()
        }
    }
}