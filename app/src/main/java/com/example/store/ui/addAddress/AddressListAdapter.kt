package com.example.store.ui.addAddress

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.AddressItem

class AddressListAdapter(var onClick:(AddressItem)->Unit):
    ListAdapter<AddressItem, AddressListAdapter.ViewHolder>(AddressDiffCallback) {

    class ViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view){
        private val tvName =view.findViewById<TextView>(R.id.tv_address_name_1)
        private val tvAddress=view.findViewById<TextView>(R.id.tv_address_location_1)

        fun bind(addressItem: AddressItem, onClick: (AddressItem) -> Unit){
            tvName.text=addressItem.name
            tvAddress.text=addressItem.address


            itemView.setOnClickListener{
                onClick.invoke(addressItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),onClick)
    }

    object AddressDiffCallback: DiffUtil.ItemCallback<AddressItem>(){
        override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return oldItem==newItem
        }


    }
}