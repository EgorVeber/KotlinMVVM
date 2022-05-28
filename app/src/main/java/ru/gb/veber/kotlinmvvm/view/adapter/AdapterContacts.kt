package ru.gb.veber.kotlinmvvm.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.gb.veber.kotlinmvvm.R
import ru.gb.veber.kotlinmvvm.view.fragment.MyContacts

class AdapterContacts() : RecyclerView.Adapter<ContactsHolder>() {

    private var myContactsList: MutableList<MyContacts> = mutableListOf()
    private lateinit var contactsClick: OnContactsClickListener

    fun setOnContactsClickListener(listener: OnContactsClickListener) {
        contactsClick = listener
    }

    fun setWeather(data: MutableList<MyContacts>) {
        myContactsList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ContactsHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.contacts_item, parent, false) as View, contactsClick
    )

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) =
        holder.bind(myContactsList[position])

    override fun getItemCount() = myContactsList.size
}

class ContactsHolder(view: View, private var cityClick: OnContactsClickListener) :
    RecyclerView.ViewHolder(view) {
    fun bind(myContacts: MyContacts) {
        itemView.apply {
            myContacts.apply {
                findViewById<TextView>(R.id.display_name).text =
                    myContacts.name + ":"
                findViewById<TextView>(R.id.phone_number).text =
                    myContacts.phone
                setOnClickListener {
                    cityClick.onContactsClick(this)
                }
            }
        }
    }
}

interface OnContactsClickListener {
    fun onContactsClick(contacts: MyContacts)
}
