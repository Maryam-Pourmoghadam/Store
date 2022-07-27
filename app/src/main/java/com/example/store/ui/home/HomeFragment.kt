package com.example.store.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.model.SharedFunctions
import com.example.store.ui.adapters.ProductListAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModels()
    private var newestProductListAdapter: ProductListAdapter? = null
    private var mostViewedProductsListAdapter: ProductListAdapter? = null
    private var bestProductsListAdapter: ProductListAdapter? = null
    private var categoryListAdapter: CategoryListadapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapters()
        observeLiveDatas(view)



        binding.btnRetry.setOnClickListener {
            homeViewModel.getPopularProducts()
            homeViewModel.getBestProducts()
            homeViewModel.getNewProducts()
            homeViewModel.getCategories()
            homeViewModel.getSaleProducts()
        }

    }

    private fun observeLiveDatas(view: View) {
        homeViewModel.newProductList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(
                        binding.shimmerLayoutNewest, binding.rvNewestProducts
                    )
                    response.data?.let { list ->
                        newestProductListAdapter!!.submitList(list.sortedByDescending { it.dateCreated })
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(
                        binding.shimmerLayoutNewest, binding.rvNewestProducts
                    )
                }
            }

        }

        homeViewModel.popularProductList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(
                        binding.shimmerLayoutMostViewed, binding.rvMostViewedProducts
                    )
                    response.data?.let { list ->
                        mostViewedProductsListAdapter!!.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(
                        binding.shimmerLayoutMostViewed, binding.rvMostViewedProducts
                    )
                }
            }

        }

        homeViewModel.bestProductList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(
                        binding.shimmerLayoutBest, binding.rvBestProducts
                    )
                    response.data?.let { list ->
                        bestProductsListAdapter!!.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(
                        binding.shimmerLayoutBest, binding.rvBestProducts
                    )

                }
            }
        }
        homeViewModel.categoryList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(
                        binding.shimmerLayoutCategories,
                        binding.rvCategories
                    )
                    response.data?.let { list ->
                        categoryListAdapter!!.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(
                        binding.shimmerLayoutCategories,
                        binding.rvCategories
                    )
                }
            }
        }

        homeViewModel.salesProduct.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    initSuccessResultViews(
                        binding.shimmerLayoutSlider, binding.vpSalesProducts
                    )
                    response.data?.let { product ->
                        val sliderListAdapter = SliderViewPagerAdapter(product.images)
                        binding.vpSalesProducts.adapter = sliderListAdapter
                        //auto sliding between images
                        lifecycleScope.launch {
                            while (true) {
                                for (i in 0..product.images.size) {
                                    delay(4000)
                                    binding.vpSalesProducts.setCurrentItem(i, true)
                                }
                            }
                        }
                    }
                }
                is NetworkResult.Error -> {
                    doErrorProgress(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    initLoadingResultViews(
                        binding.shimmerLayoutSlider, binding.vpSalesProducts
                    )
                }
            }
        }
    }

    private fun doErrorProgress(errorMessage: String, view: View) {
        initErrorResultViews()
        SharedFunctions.showSnackBar(errorMessage, view)
    }

    private fun setAdapters() {
        newestProductListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvNewestProducts.adapter = newestProductListAdapter

        mostViewedProductsListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvMostViewedProducts.adapter = mostViewedProductsListAdapter

        bestProductsListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvBestProducts.adapter = bestProductsListAdapter

        categoryListAdapter = CategoryListadapter {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(it)
            findNavController().navigate(action)
        }
        binding.rvCategories.adapter = categoryListAdapter
    }


    private fun navigateToDetailsFragment(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(id)
        findNavController().navigate(action)
    }

    private fun initSuccessResultViews(
        shimmer: ShimmerFrameLayout,
        detailsLayout: ViewGroup
    ) {
        shimmer.visibility = View.GONE
        detailsLayout.visibility = View.VISIBLE
        binding.llErrorConnection.visibility = View.GONE
        binding.llHomeDetails.visibility = View.VISIBLE
    }

    private fun initLoadingResultViews(
        shimmer: ShimmerFrameLayout,
        detailsLayout: ViewGroup
    ) {
        shimmer.visibility = View.VISIBLE
        detailsLayout.visibility = View.GONE
        binding.llErrorConnection.visibility = View.GONE
        binding.llHomeDetails.visibility = View.VISIBLE

    }

    private fun initErrorResultViews() {
        binding.llHomeDetails.visibility = View.GONE
        binding.llErrorConnection.visibility = View.VISIBLE
    }


}