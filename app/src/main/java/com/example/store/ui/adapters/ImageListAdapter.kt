package com.example.store.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.model.Image

class ImageListAdapter :ListAdapter<Image,ImageListAdapter.ViewHolder>(ImageDiffCallback){
    class ViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view){
        private val ivProductImage=view.findViewById<ImageView>(R.id.iv_product_image_details)

        fun bind(image:Image){
            Glide.with(context)
                .load(image.src)
                .override(800,700)
                .centerCrop()
                .into(ivProductImage)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       return holder.bind(getItem(position))
    }

    object ImageDiffCallback: DiffUtil.ItemCallback<Image>(){
        override fun areItemsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: Image, newItem: Image): Boolean {
            return oldItem==newItem
        }

    }
}