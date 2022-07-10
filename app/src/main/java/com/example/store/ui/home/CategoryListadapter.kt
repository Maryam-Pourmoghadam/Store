package com.example.store.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.CategoryItem

class CategoryListadapter(var onClick:(Int)->Unit):
    ListAdapter<CategoryItem, CategoryListadapter.ViewHolder>(CategoryDiffCallback) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvCategoryName=view.findViewById<TextView>(R.id.tv_category_name)

        fun bind(category: CategoryItem, onClick: (Int) -> Unit){
            tvCategoryName.text=category.name

            itemView.setOnClickListener{
                onClick.invoke(category.id)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),onClick)
    }

    object CategoryDiffCallback: DiffUtil.ItemCallback<CategoryItem>(){
        override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
            return oldItem==newItem
        }


    }
}