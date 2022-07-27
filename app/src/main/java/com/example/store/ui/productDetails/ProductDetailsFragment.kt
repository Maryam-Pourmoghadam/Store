package com.example.store.ui.productDetails

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentProductDetailsBinding
import com.example.store.model.ProductItem
import com.example.store.model.ReviewItem
import com.example.store.model.SharedFunctions
import com.example.store.ui.adapters.ImageListAdapter
import com.example.store.ui.adapters.ProductListAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDetailsBinding
    private val productDetailsViewModel: ProductDetailsViewModel by viewModels()
    private var reviewsAdapter: ReviewListAdapter? = null
    private var imageListAdapter: ImageListAdapter? = null
    private var relatedListAdapter: ProductListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productDetailsViewModel.productId = it.getInt("id")
            productDetailsViewModel.getProductDetails()
            productDetailsViewModel.getProductReviews()
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters(view)
        observeLiveDatas(view)
        setButtonsListener()


    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setButtonsListener() {
        binding.btnAddReview.setOnClickListener {
            productDetailsViewModel.addReviewBtnIsActive.value =
                !(productDetailsViewModel.addReviewBtnIsActive.value)!!

            if (!productDetailsViewModel.editReviewIsEnable)
                makeReviewFieldsEnable()
        }

        binding.btnCancel.setOnClickListener {
            setReviewFieldsToDefault()
        }

        binding.btnApplyReview.setOnClickListener {
            //for sending new review
            if (!productDetailsViewModel.editReviewIsEnable) {
                if (areValidReviewFields()) {
                    val reviewItem = makeReviewItemByUserInputs()
                    productDetailsViewModel.sendReview(reviewItem)
                    setReviewFieldsToDefault()
                }
            } else {
                //for editing selected review
                productDetailsViewModel.editReview(
                    productDetailsViewModel.editReviewId,
                    binding.etReviewerReview.text.toString(), getRating()
                )
                setReviewFieldsToDefault()
            }
        }

        binding.btnAddToShoppingCart.setOnClickListener {
            productDetailsViewModel.setProductInSharedPref(requireActivity())
        }

        binding.btnRetryDetailsfrgmnt.setOnClickListener {
            val productItem = productDetailsViewModel.getProductDetails()
            if (productItem != null) {
                productDetailsViewModel.getRelatedProducts(productItem)
                productDetailsViewModel.getProductReviews()
            }
        }
    }

    private fun setAdapters(view: View) {
        imageListAdapter = ImageListAdapter()
        binding.rvProductImages.adapter = imageListAdapter

        relatedListAdapter = ProductListAdapter {
            val action = ProductDetailsFragmentDirections.actionProductDetailsFragmentSelf(it)
            findNavController().navigate(action)
        }
        binding.rvRelatedProducts.adapter = relatedListAdapter

        reviewsAdapter = ReviewListAdapter(
            //delete listener
            {
                SharedFunctions.showSnackBar("جهت حذف نظر منتظر بمانید", view)
                val reviewList = productDetailsViewModel.reviewList
                if (reviewList != null) {
                    if (reviewList.contains(it)) {
                        productDetailsViewModel.deleteReview(it.id)

                    } else {
                        SharedFunctions.showSnackBar(
                            "این محصول قبلا حذف شده یا در سرور موجود نیست",
                            view
                        )
                    }
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

    }

    private fun observeLiveDatas(view: View) {
        productDetailsViewModel.productDetails.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(binding.shimmerLayout, binding.rvProductImages)
                    binding.btnAddToShoppingCart.isEnabled = true
                    response.data?.let { product ->
                        imageListAdapter!!.submitList(product.images)
                        initViews(product)
                        productDetailsViewModel.getRelatedProducts(product)
                        productDetailsViewModel.getProductReviews()
                        productDetailsViewModel.setValues(product)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(binding.shimmerLayout, binding.rvProductImages)
                    binding.btnAddToShoppingCart.isEnabled = false
                }
            }

        }


        productDetailsViewModel.relatedProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(binding.shimmerRelatedProduct, binding.rvRelatedProducts)
                    response.data?.let { list ->
                        relatedListAdapter!!.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    binding.llErrorConnection.visibility = View.GONE
                    binding.clProductDetails.visibility = View.VISIBLE
                    binding.rvRelatedProducts.visibility = View.GONE
                    binding.shimmerRelatedProduct.visibility = View.VISIBLE

                }
            }
        }


        productDetailsViewModel.productReviews.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(binding.shimmerReview, binding.rvReviews)
                    response.data.let { list ->
                        reviewsAdapter!!.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(binding.shimmerReview, binding.rvReviews)
                }
            }
        }

        productDetailsViewModel.sendReviewResponse.observe(viewLifecycleOwner) { response ->
            progressServerReviewResponse(
                "نظر شما با موفقیت ارسال شد",
                "جهت ثبت نظر منتظر بمانید",
                "مشکلی در ارسال نظر رخ داده است مجددا تلاش کنید",
                response, view
            )
        }

        productDetailsViewModel.deleteReviewResponse.observe(viewLifecycleOwner) { response ->
            progressServerReviewResponse(
                "نظر شما با موفقیت حذف شد",
                "جهت حذف نظر منتظر بمانید",
                "مشکلی در حذف نظر رخ داده است مجددا تلاش کنید",
                response, view
            )
        }

        productDetailsViewModel.updateReviewResponse.observe(viewLifecycleOwner) { response ->
            progressServerReviewResponse(
                "نظر شما با موفقیت ویرایش شد",
                "جهت ویرایش نظر منتظر بمانید",
                "مشکلی در ویرایش نظر رخ داده است مجددا تلاش کنید",
                response, view
            )
        }

        productDetailsViewModel.addReviewBtnIsActive.observe(viewLifecycleOwner) {
            binding.llAddReview.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

    }

    private fun doErrorProgress(message: String, view: View) {
        initErrorResultViews()
        SharedFunctions.showSnackBar(message, view)
    }

    private fun progressServerReviewResponse(
        successMessage: String,
        loadingMessage: String,
        errorMessage: String,
        response: NetworkResult<ReviewItem>,
        view: View
    ) {
        when (response) {
            is NetworkResult.Success -> {
                SharedFunctions.showSnackBar(successMessage, view)
                productDetailsViewModel.getProductReviews()
            }
            is NetworkResult.Error -> {
                SharedFunctions.showSnackBar(response.message.toString() + " " + errorMessage, view)
            }
            is NetworkResult.Loading -> {
                SharedFunctions.showSnackBar(loadingMessage, view)
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
            else -> binding.rbtn0.isChecked = true
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
            LocalDateTime.now().toString(),
            rating,
            review,
            productDetailsViewModel.productId,
            0,
            email
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

    private fun initSuccessResultViews(
        shimmer: ShimmerFrameLayout,
        detailsLayout: ViewGroup
    ) {
        shimmer.visibility = View.GONE
        detailsLayout.visibility = View.VISIBLE
        binding.llErrorConnection.visibility = View.GONE
        binding.clProductDetails.visibility = View.VISIBLE
    }

    private fun initLoadingResultViews(
        shimmer: ShimmerFrameLayout,
        detailsLayout: ViewGroup
    ) {
        shimmer.visibility = View.VISIBLE
        detailsLayout.visibility = View.INVISIBLE
        binding.llErrorConnection.visibility = View.GONE
        binding.clProductDetails.visibility = View.VISIBLE

    }

    private fun initErrorResultViews() {
        binding.llErrorConnection.visibility = View.VISIBLE
        binding.clProductDetails.visibility = View.GONE
    }


    private fun setReviewFieldsToDefault() {
        binding.etReviewerName.setText("")
        binding.etReviewerEmail.setText("")
        binding.etReviewerReview.setText("")
        binding.rbtn0.isChecked = true
        productDetailsViewModel.editReviewId = -1
        productDetailsViewModel.editReviewIsEnable = false
        productDetailsViewModel.addReviewBtnIsActive.value = false
    }

}