package com.example.store.ui.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.store.databinding.FragmentProductDetailsBinding
import com.example.store.model.ProductItem
import com.example.store.model.Status
import com.example.store.ui.adapters.ImageListAdapter
import dagger.hilt.android.AndroidEntryPoint

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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImageListAdapter()
        binding.rvProductImages.adapter = adapter
        productDetailsViewModel.productDetails.observe(viewLifecycleOwner) {
            adapter.submitList(it.images)
            initViews(it)
        }

        productDetailsViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }

        binding.btnRetryDetailsfrgmnt.setOnClickListener {
            productDetailsViewModel.getProductDetails(productID)
        }

    }

    private fun initViews(productDetail: ProductItem) {
        binding.tvName.text = productDetail.name
        binding.tvPrice.text = productDetail.price
        binding.tvDateCreated.text = productDetail.dateCreated
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
            }
            else -> {
                binding.llErrorConnection.visibility = View.GONE
                binding.clProductDetails.visibility = View.VISIBLE
                binding.shimmerLayout.visibility = View.GONE
                binding.rvProductImages.visibility = View.VISIBLE
            }
        }
    }
}