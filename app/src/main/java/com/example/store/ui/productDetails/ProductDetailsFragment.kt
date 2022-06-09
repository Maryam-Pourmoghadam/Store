package com.example.store.ui.productDetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.store.R
import com.example.store.databinding.FragmentProductDetailsBinding
import com.example.store.model.ProductItem
import com.example.store.model.Status
import com.example.store.ui.adapters.ImageListAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProductDetailsFragment : Fragment() {
    lateinit var binding: FragmentProductDetailsBinding
    val productDetailsViewModel: ProductDetailsViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val id = it.getInt("id")
            productDetailsViewModel.getProductDetails(id)
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
        var isConnected = false
        val adapter = ImageListAdapter()
        binding.rvProductImages.adapter = adapter
        productDetailsViewModel.productDetails.observe(viewLifecycleOwner) {
            if (isConnected) {
                adapter.submitList(it.images)
                initViews(it)
            }
        }

        productDetailsViewModel.status.observe(viewLifecycleOwner) {
            isConnected = it != Status.ERROR
            if (it == Status.ERROR)
                Snackbar.make(view,  R.string.network_error, Snackbar.LENGTH_SHORT)
                    .setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()

        }

    }

    private fun initViews(productDetail: ProductItem) {
        binding.tvName.text = productDetail.name
        binding.tvPrice.text = productDetail.price
        binding.tvDateCreated.text = productDetail.dateCreated
        binding.tvRatingCount.text = productDetail.ratingCount.toString()
        binding.tvDescription.text = productDetail.description
        binding.tvPurchasable.text = if (productDetail.purchasable) {
            "موجود"
        } else {
            "ناموجود"
        }
    }
}