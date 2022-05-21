package ru.gb.kotlinapp.view.history

import android.util.Log
import android.view.*
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.history_item.view.*
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.model.Weather

class HistoryAdapter : RecyclerView.Adapter<HistoryHolder>() {
    private var data: List<Weather> = arrayListOf()

    private lateinit var listner: ClickHistory

    fun setOnNoteCliclListner(listner: ClickHistory) {
        this.listner = listner
    }

    fun setData(data: List<Weather>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        return HistoryHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.history_item, parent, false) as View, this.listner
        )
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount() = data.size

}

interface ClickHistory {
    fun deleteHistoryId(id: Int)
    fun deleteHistory()
}

class HistoryHolder(view: View, var listner: ClickHistory) : RecyclerView.ViewHolder(view),
    PopupMenu.OnMenuItemClickListener {

    private lateinit var weather: Weather
    var popupMenu = PopupMenu(itemView.context, itemView, Gravity.RIGHT).also {
        it.inflate(R.menu.menu_history)
        it.setOnMenuItemClickListener(this)
    }

    fun bind(data: Weather) {
        this.weather = data
        if (layoutPosition != RecyclerView.NO_POSITION) {
            itemView.recyclerViewItem.text =
                String.format("%s %d %s", data.city.cityName, data.temperature, data.condition)
            itemView.setOnClickListener {
                popupMenu.show()
            }
        }
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        Log.d("TAG", "onMenuItemClick() called with: menuItem = $menuItem")
        when (menuItem.itemId) {
            R.id.menu_history_delete -> {
                listner.deleteHistoryId(adapterPosition)
            }
            R.id.menu_history_delete_all -> {
                listner.deleteHistory()
            }
        }
        return true
    }
}