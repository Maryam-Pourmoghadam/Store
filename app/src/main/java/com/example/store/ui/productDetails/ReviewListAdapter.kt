package com.example.store.ui.productDetails

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.store.R
import com.example.store.model.ProductOrderItem
import com.example.store.model.ReviewItem
import com.example.store.ui.shoppingCart.onClickOrderDelete

typealias onClickReviewEdit=(ReviewItem)->Unit
typealias onClickReviewDelete=(ReviewItem)->Unit
class ReviewListAdapter(var onClickDelete: onClickReviewDelete, var onClickEdit : onClickReviewEdit):
    ListAdapter<ReviewItem, ReviewListAdapter.ViewHolder>(ReviewDiffCallback) {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val tvReviewerName=view.findViewById<TextView>(R.id.tv_reviewer_name)
        private val tvReview=view.findViewById<TextView>(R.id.tv_review)
        private val tvReviewDate=view.findViewById<TextView>(R.id.tv_review_date)
        private val reviewrRatingBar=view.findViewById<RatingBar>(R.id.ratingBar)
        private val btnDeleteReview=view.findViewById<ImageButton>(R.id.ibtn_delete_review)
        private val btnEditReview=view.findViewById<ImageButton>(R.id.ibtn_edit_review)

        fun bind(reviewItem: ReviewItem
        ,onClickDelete: onClickReviewDelete,
        onClickEdit: onClickReviewEdit){
            tvReviewerName.text=reviewItem.reviewer
            tvReview.text=reviewItem.review.replace(Regex("br|p|<|>|/"), "")
            tvReviewDate.text=reviewItem.dateCreated.replace("T", " ")
            reviewrRatingBar.rating=reviewItem.rating.toFloat()
            btnDeleteReview.setOnClickListener {
                onClickDelete.invoke(reviewItem)
            }
            btnEditReview.setOnClickListener {
                onClickEdit.invoke(reviewItem)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.review_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position),onClickDelete, onClickEdit)
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