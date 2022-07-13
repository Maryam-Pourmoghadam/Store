package com.example.store.ui.productDetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentProductDetailsBinding
import com.example.store.model.ProductItem
import com.example.store.model.ReviewItem
import com.example.store.model.Status
import com.example.store.ui.adapters.ImageListAdapter
import com.example.store.ui.adapters.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    var productID = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productID = it.getInt("id")
            productDetailsViewModel.getProductDetails(productID)
            productDetailsViewModel.getProductReviews(productID.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setReviewFieldsToDefault()
    }

    private fun setReviewFieldsToDefault() {
        binding.etReviewerName.setText("")
        binding.etReviewerEmail.setText("")
        binding.etReviewerReview.setText("")
        binding.rbtn0.isChecked=true
        productDetailsViewModel.editReviewId=-1
        productDetailsViewModel.editReviewIsEnable=false
        productDetailsViewModel.addReviewBtnIsActive.value=false
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val imageListadapter = ImageListAdapter()
        binding.rvProductImages.adapter = imageListadapter
        productDetailsViewModel.productDetails.observe(viewLifecycleOwner) {
            imageListadapter.submitList(it.images)
            initViews(it)
            productDetailsViewModel.getRelatedProducts(it)
        }

        val relatedListAdapter = ProductListAdapter {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentSelf(it)
            findNavController().navigate(action)
        }
        binding.rvRelatedProducts.adapter = relatedListAdapter
        productDetailsViewModel.relatedProducts.observe(viewLifecycleOwner) {
            relatedListAdapter.submitList(it)
        }

        productDetailsViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }

        val reviewsAdapter = ReviewListAdapter(
            //delete listener
            {
            Toast.makeText(requireContext(), "جهت حذف نظر منتظر بمانید", Toast.LENGTH_LONG).show()
            val reviewList=productDetailsViewModel.reviewList
            if (reviewList.contains(it)) {
                productDetailsViewModel.deleteReview(it.id, requireContext())

            } else {
                Toast.makeText(
                    requireContext(),
                    "این محصول قبلا حذف شده یا در سرور موجود نیست",
                    Toast.LENGTH_LONG
                ).show()
            }
        },
            //edit listener
            {
            productDetailsViewModel.addReviewBtnIsActive.value = true
            setSelectedReviewDetailsInFields(it)
            makeReviewFieldsDisable()
            productDetailsViewModel.editReviewIsEnable = true
            productDetailsViewModel.editReviewId = it.id
        })
        binding.rvReviews.adapter = reviewsAdapter

        productDetailsViewModel.productReviews.observe(viewLifecycleOwner) {
            reviewsAdapter.submitList(it)
        }



        binding.btnRetryDetailsfrgmnt.setOnClickListener {
            val productItem = productDetailsViewModel.getProductDetails(productID)
            if (productItem != null) {
                productDetailsViewModel.getRelatedProducts(productItem)
            }
            productDetailsViewModel.getProductReviews(productID.toString())
        }

        binding.btnAddToShoppingCart.setOnClickListener {
            productDetailsViewModel.setProductInSharedPref(requireActivity(), productID)
        }

        binding.btnApplyReview.setOnClickListener {
                //for sending new review
                if (!productDetailsViewModel.editReviewIsEnable) {
                    if (areValidReviewFields()) {
                        val reviewItem = makeReviewItemByUserInputs()
                        productDetailsViewModel.sendReview(reviewItem, requireContext())
                        setReviewFieldsToDefault()
                    }
                } else {
                    //for editing selected review
                    productDetailsViewModel.editReview(
                        productDetailsViewModel.editReviewId,
                        binding.etReviewerReview.text.toString(), getRating(), requireContext()
                    )
                    setReviewFieldsToDefault()
                }


        }
        binding.btnAddReview.setOnClickListener {
            productDetailsViewModel.addReviewBtnIsActive.value =
                !(productDetailsViewModel.addReviewBtnIsActive.value)!!

            if (!productDetailsViewModel.editReviewIsEnable)
                makeReviewFieldsEnable()
        }
        binding.btnCancel.setOnClickListener {
            setReviewFieldsToDefault()
        }
        productDetailsViewModel.addReviewBtnIsActive.observe(viewLifecycleOwner){
            binding.llAddReview.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }


    }

    private fun setSelectedReviewDetailsInFields(reviewItem: ReviewItem) {
        binding.etReviewerName.setText(reviewItem.reviewer)
        binding.etReviewerEmail.setText(reviewItem.reviewerEmail)
        binding.etReviewerReview.setText(reviewItem.review.replace(Regex("br|p|<|>|/"), ""))
        when (reviewItem.rating) {
            1 -> binding.rbtn1.isChecked = true
            2 -> binding.rbtn2.isChecked = true
            3 -> binding.rbtn3.isChecked = true
            4 -> binding.rbtn4.isChecked = true
            5 -> binding.rbtn5.isChecked = true
            else->binding.rbtn0.isChecked = true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun makeReviewItemByUserInputs(): ReviewItem {
        var review = ""
        var email = ""
        if (binding.etReviewerEmail.text.isNotBlank())
            email = binding.etReviewerEmail.text.toString()
        if (binding.etReviewerReview.text.isNotBlank())
            review = binding.etReviewerReview.text.toString()
        val rating = getRating()
        return (ReviewItem(
            binding.etReviewerName.text.toString(),
            LocalDateTime.now().toString(), rating, review, productID, 0, email
        ))
    }

    private fun getRating(): Int {
        if (binding.rbtn1.isChecked)
            return 1
        if (binding.rbtn2.isChecked)
            return 2
        if (binding.rbtn3.isChecked)
            return 3
        if (binding.rbtn4.isChecked)
            return 4
        if (binding.rbtn5.isChecked)
            return 5
        return 0
    }

    private fun makeReviewFieldsDisable() {
        binding.etReviewerName.isEnabled = false
        binding.etReviewerEmail.isEnabled = false
    }

    private fun makeReviewFieldsEnable() {
        binding.etReviewerName.isEnabled = true
        binding.etReviewerEmail.isEnabled = true
    }

    private fun initViews(productDetail: ProductItem) {
        binding.tvName.text = productDetail.name
        binding.tvPrice.text = productDetail.price
        binding.tvDateCreated.text = productDetail.dateCreated.replace("T", " ")
        binding.tvRatingCount.text = productDetail.ratingCount.toString()
        binding.tvDescription.text = productDetail.description.replace(Regex("br|p|<|>|/"), "")
        binding.tvPurchasable.text = if (productDetail.purchasable) {
            "موجود"
        } else {
            "ناموجود"
        }
    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llErrorConnection.visibility = View.VISIBLE
                binding.clProductDetails.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llErrorConnection.visibility = View.GONE
                binding.clProductDetails.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.VISIBLE
                binding.rvProductImages.visibility = View.INVISIBLE
                binding.btnAddToShoppingCart.isEnabled = false
            }
            else -> {
                binding.llErrorConnection.visibility = View.GONE
                binding.clProductDetails.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.rvProductImages.visibility = View.VISIBLE
                binding.btnAddToShoppingCart.isEnabled = true
            }
        }
    }

    private fun areValidReviewFields(): Boolean {
        if (binding.etReviewerName.text.isNullOrBlank()) {
            binding.etReviewerName.error = "فیلد را پر کنید"
            return false
        }
        /* if (binding.etReviewerEmail.text.isNullOrBlank()) {
             binding.etReviewerEmail.error = "فیلد را پر کنید"
             return false
         }
         if (!binding.etReviewerEmail.text.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) {
             binding.etReviewerEmail.error = "فرم ایمیل صحیح نمی باشد"
             return false
         }*/
        if (binding.etReviewerReview.text.isNullOrBlank()) {
            binding.etReviewerReview.error = "فیلد را پر کنید"
        }
        return true
    }
}