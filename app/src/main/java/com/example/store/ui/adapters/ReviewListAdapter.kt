package com.example.store.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.CategoryItem
import com.example.store.model.ReviewItem

class ReviewListAdapter():
    ListAdapter<ReviewItem, ReviewListAdapter.ViewHolder>(ReviewDiffCallback) {

    class ViewHolder(view: View, private val context: Context): RecyclerView.ViewHolder(view){
        val tvReviewerName=view.findViewById<TextView>(R.id.tv_reviewer_name)
        val tvReview=view.findViewById<TextView>(R.id.tv_review)
        val tvReviewDate=view.findViewById<TextView>(R.id.tv_review_date)
        val tvReviewrRating=view.findViewById<TextView>(R.id.tv_reviewer_rating)

        fun bind(reviewItem: ReviewItem){
            tvReviewerName.text=reviewItem.reviewer
            tvReview.text=reviewItem.review.replace(Regex("br|p|<|>|/"), "")
            tvReviewDate.text=reviewItem.dateCreated.replace("T", " ")
            tvReviewrRating.text= reviewItem.rating.toString()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ViewHolder(view, parent.context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object ReviewDiffCallback: DiffUtil.ItemCallback<ReviewItem>(){
        override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
            return oldItem==newItem
        }


    }
}