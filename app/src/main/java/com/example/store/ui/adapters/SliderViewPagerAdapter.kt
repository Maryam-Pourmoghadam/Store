package com.example.store.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.store.R
import com.example.store.model.Image
import com.google.android.material.imageview.ShapeableImageView

class SliderViewPagerAdapter(private val imageList:List<Image>) :
    RecyclerView.Adapter<SliderViewPagerAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sliderImageView = itemView.findViewById<ShapeableImageView>(R.id.iv_slider_image)
        fun bind(image:Image) {
            Glide.with(itemView.context)
                .load(image.src)
                .into(sliderImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_slider_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
       holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }


}