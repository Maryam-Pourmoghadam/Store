package com.example.store.ui.productDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.ReviewItem


class ReviewListAdapter():
    ListAdapter<ReviewItem, ReviewListAdapter.ViewHolder>(ReviewDiffCallback) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvReviewerName=view.findViewById<TextView>(R.id.tv_reviewer_name)
        private val tvReview=view.findViewById<TextView>(R.id.tv_review)
        private val tvReviewDate=view.findViewById<TextView>(R.id.tv_review_date)
        private val reviewrRatingBar=view.findViewById<RatingBar>(R.id.ratingBar)

        fun bind(reviewItem: ReviewItem){
            tvReviewerName.text=reviewItem.reviewer
            tvReview.text=reviewItem.review.replace(Regex("br|p|<|>|/"), "")
            tvReviewDate.text=reviewItem.dateCreated.replace("T", " ")
            reviewrRatingBar.rating=reviewItem.rating.toFloat()

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
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