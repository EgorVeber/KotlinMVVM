package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Hours
import java.text.SimpleDateFormat

class AdapterHour : RecyclerView.Adapter<HolderHour>() {

    private var hoursData: List<Hours> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = HolderHour(
        LayoutInflater.from(parent.context).inflate(R.layout.hour_item, parent, false) as View
    )


    fun setWeather(hoursData: List<Hours>) {
        this.hoursData = hoursData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: HolderHour, position: Int) =
        holder.bind(hoursData[position])

    override fun getItemCount() = hoursData.size
}

class HolderHour(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val hourTime: TextView = itemView.findViewById(R.id.hour_time)
    private val hourWeather: TextView = itemView.findViewById(R.id.hour_weather)
    fun bind(hours: Hours) {
        hourTime.text = SimpleDateFormat("H:m").format(hours.hour)
        hourWeather.text = hours.temp.toString()
    }
}
