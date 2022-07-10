package com.example.store.ui.shoppingCart

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

typealias onClickOrderCount=(ProductOrderItem)->Unit
typealias onClickOrderDelete=(ProductOrderItem)->Unit
class OrderListAdapter(var plusClick:onClickOrderCount, var minusClick:onClickOrderCount
,var deleteClick:onClickOrderDelete
    //var onClickEdit: (Int) -> Unit
) :
    ListAdapter<ProductOrderItem, OrderListAdapter.ViewHolder>(OrderDiffCallback) {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvOrderName = view.findViewById<TextView>(R.id.tv_order_name)
        private val tvOrderCount = view.findViewById<TextView>(R.id.tv_order_count)
        private val tvOrderPrice = view.findViewById<TextView>(R.id.tv_order_price)
        private val btnPlus = view.findViewById<Button>(R.id.btn_plus)
        private val btnMinus = view.findViewById<Button>(R.id.btn_minus)
        private val btnDelete = view.findViewById<Button>(R.id.btn_delete)

        fun bind(
            orderItem: ProductOrderItem,
            onClickPlus:onClickOrderCount,
            onClickMinus:onClickOrderCount,
            onClickDelete:onClickOrderDelete
            //onClickEdit: (Int) -> Unit
        ) {

            tvOrderName.text = orderItem.name
            tvOrderPrice.text = orderItem.price.toString()
            tvOrderCount.text = orderItem.quantity.toString()

            btnPlus.setOnClickListener {
                onClickPlus.invoke(orderItem)
            }

            btnMinus.setOnClickListener {
                onClickMinus.invoke(orderItem)
            }
            btnDelete.setOnClickListener {
                onClickDelete.invoke(orderItem)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.shopping_cart_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), plusClick,minusClick,deleteClick)
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