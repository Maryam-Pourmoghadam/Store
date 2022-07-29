package com.example.store.ui.addAddress

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.AddressItem

typealias onClickAddressDelete=(AddressItem)->Unit
class AddressListAdapter(var onWholeClick: (AddressItem)->Unit, var onDeleteClick:onClickAddressDelete) :
    ListAdapter<AddressItem, AddressListAdapter.ViewHolder>(AddressDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvName = view.findViewById<TextView>(R.id.tv_address_name_1)
        private val tvAddress = view.findViewById<TextView>(R.id.tv_address_location_1)
        private val ibtnDeleteAddress = view.findViewById<ImageButton>(R.id.ibtn_delete_address)

        fun bind(addressItem: AddressItem, onWholeClick: (AddressItem) -> Unit,onDeleteClick: onClickAddressDelete) {
            tvName.text = addressItem.name
            tvAddress.text = addressItem.address

            itemView.setOnClickListener {
                onWholeClick.invoke(addressItem)
            }
            ibtnDeleteAddress.setOnClickListener {
                onDeleteClick.invoke(addressItem)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onWholeClick,onDeleteClick)
    }

    object AddressDiffCallback : DiffUtil.ItemCallback<AddressItem>() {
        override fun areItemsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AddressItem, newItem: AddressItem): Boolean {
            return oldItem == newItem
        }


    }
}