package com.example.store.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.ProductOrderItem

class OrderListAdapter(
    var onClickEdit: (Int) -> Unit
) :
    ListAdapter<ProductOrderItem, OrderListAdapter.ViewHolder>(OrderDiffCallback) {

    class ViewHolder(view: View, private val context: Context) : RecyclerView.ViewHolder(view) {
        val tvOrderName = view.findViewById<TextView>(R.id.tv_order_name)
        val tvOrderCount = view.findViewById<TextView>(R.id.tv_order_count)
        val tvOrderPrice = view.findViewById<TextView>(R.id.tv_order_price)
        val btnEdit = view.findViewById<Button>(R.id.btn_edit_order)

        fun bind(
            orderItem: ProductOrderItem,
            onClickEdit: (Int) -> Unit
        ) {
            tvOrderName.text = orderItem.name
            tvOrderPrice.text = orderItem.price.toString()
            tvOrderCount.text = orderItem.quantity.toString()

            btnEdit.setOnClickListener {
                onClickEdit.invoke(orderItem.productId)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_cart_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onClickEdit)
    }

    object OrderDiffCallback : DiffUtil.ItemCallback<ProductOrderItem>() {
        override fun areItemsTheSame(
            oldItem: ProductOrderItem,
            newItem: ProductOrderItem
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: ProductOrderItem,
            newItem: ProductOrderItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}