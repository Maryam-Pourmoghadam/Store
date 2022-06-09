package com.example.store.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.ProductItem
import com.squareup.picasso.Picasso


class ProductListAdapter(var onClick:(Int)->Unit):ListAdapter<ProductItem, ProductListAdapter.ViewHolder>(ProductDiffCallback) {

    class ViewHolder(view: View,private val context:Context):RecyclerView.ViewHolder(view){
        val tvName=view.findViewById<TextView>(R.id.tv_product_name)
        val ivProductImg=view.findViewById<ImageView>(R.id.iv_product_image)

        fun bind(productItem: ProductItem,onClick: (Int) -> Unit){
            tvName.text=productItem.name
            Picasso.get()
                .load(productItem.images[0].src)
                .centerCrop()
                .resize(400,400)
                .into(ivProductImg)
           /* Glide.with(context)
                .load(productItem.images[0].src)
                .override(400,400)
                .centerCrop()
                .into(ivProductImg)*/

            itemView.setOnClickListener{
                onClick.invoke(productItem.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.products_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bind(getItem(position),onClick)
    }

    object ProductDiffCallback:DiffUtil.ItemCallback<ProductItem>(){
        override fun areItemsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ProductItem, newItem: ProductItem): Boolean {
            return oldItem==newItem
        }

    }
}