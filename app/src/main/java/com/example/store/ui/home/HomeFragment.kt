package com.example.store.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel by viewModels()
    var mSliderImageListSize=0
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

       /* val onImageChangeCallback= object :ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                setupIndicators(position)
            }
        }
        binding.vpSalesProducts?.registerOnPageChangeCallback(onImageChangeCallback)*/


        val newestProductListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvNewestProducts.adapter = newestProductListAdapter

        val mostViewedProductsListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvMostViewedProducts.adapter = mostViewedProductsListAdapter

        val bestProductsListAdapter = ProductListAdapter {
            navigateToDetailsFragment(it)
        }
        binding.rvBestProducts.adapter = bestProductsListAdapter

        val categoryListadapter = CategoryListadapter {
            val action = HomeFragmentDirections.actionHomeFragmentToCategoriesFragment(it)
            findNavController().navigate(action)
        }
        binding.rvCategories.adapter = categoryListadapter



        homeViewModel.productList.observe(viewLifecycleOwner) { list ->
            newestProductListAdapter.submitList(list.sortedByDescending { it.dateCreated })
        }
        homeViewModel.popularProductList.observe(viewLifecycleOwner) {
            mostViewedProductsListAdapter.submitList(it)
        }

        homeViewModel.bestProductList.observe(viewLifecycleOwner) {
            bestProductsListAdapter.submitList(it)
        }
        homeViewModel.categoryList.observe(viewLifecycleOwner) {
            categoryListadapter.submitList(it)

        }
        homeViewModel.salesProductImageSrc.observe(viewLifecycleOwner) {
            mSliderImageListSize=it.size

            val sliderListAdapter = SliderViewPagerAdapter(it)
            binding.vpSalesProducts.adapter = sliderListAdapter


            //auto sliding between images
            lifecycleScope.launch {
                while (true) {
                    for (i in 0..it.size) {
                        delay(4000)
                        binding.vpSalesProducts.setCurrentItem(i, true)
                    }
                }
            }
        }



        homeViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)

        }

        binding.btnRetry.setOnClickListener {
            homeViewModel.getPopularProducts()
            homeViewModel.getBestProducts()
            homeViewModel.getNewProducts()
            homeViewModel.getCategories()
        }

    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llHomeDetails.visibility = View.GONE
                binding.llErrorConnection.visibility = View.VISIBLE
            }
            Status.LOADING -> {
                binding.llHomeDetails.visibility = View.VISIBLE
                binding.llErrorConnection.visibility = View.GONE
                binding.shimmerLayoutSlider.visibility  = View.VISIBLE
                binding.shimmerLayoutCategories.visibility  = View.VISIBLE
                binding.shimmerLayoutNewest.visibility  = View.VISIBLE
                binding.shimmerLayoutMostViewed.visibility  = View.VISIBLE
                binding.shimmerLayoutBest.visibility  = View.VISIBLE
                binding.rvCategories.visibility = View.GONE
                binding.rvNewestProducts.visibility = View.GONE
                binding.rvBestProducts.visibility = View.GONE
                binding.rvMostViewedProducts.visibility = View.GONE
                binding.vpSalesProducts.visibility=View.GONE
            }
            else -> {
                binding.llErrorConnection.visibility = View.GONE
                binding.llHomeDetails.visibility = View.VISIBLE
                binding.shimmerLayoutSlider.visibility  = View.GONE
                binding.shimmerLayoutCategories.visibility  = View.GONE
                binding.shimmerLayoutNewest.visibility  = View.GONE
                binding.shimmerLayoutMostViewed.visibility  = View.GONE
                binding.shimmerLayoutBest.visibility  = View.GONE
                binding.rvCategories.visibility = View.VISIBLE
                binding.rvNewestProducts.visibility = View.VISIBLE
                binding.rvBestProducts.visibility = View.VISIBLE
                binding.rvMostViewedProducts.visibility = View.VISIBLE
                binding.vpSalesProducts.visibility=View.VISIBLE


            }
        }
    }

    private fun navigateToDetailsFragment(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(id)
        findNavController().navigate(action)
    }

   /* private fun setupIndicators(pagePosition:Int) {
        val indicators = arrayOfNulls<TextView>(mSliderImageListSize)
        for (i in indicators.indices){
            indicators[i]=TextView(this.context)
            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
                indicators[i]?.text=Html.fromHtml("&#8226",Html.FROM_HTML_MODE_LEGACY)
            }else{
                indicators[i]?.text=Html.fromHtml("&#8226")
            }
            indicators[i].let {
               it?.textSize=38f
                it?.setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
            }
            binding.llIndicator?.addView(indicators[i])
        }

        if (indicators.isNotEmpty()){
            indicators[pagePosition]?.setTextColor(ContextCompat.getColor(requireContext(),R.color.black))
        }

    }
*/
    /*fun setShimmersVisible(){
        binding.shimmerLayoutBestProducts.visibility = View.VISIBLE
        binding.shimmerLayoutCategory?.visibility = View.VISIBLE
        binding.shimmerLayoutMostViewed.visibility = View.VISIBLE
        binding.shimmerLayoutNewest.visibility = View.VISIBLE
        binding.rvNewestProducts.visibility = View.GONE
        binding.rvMostViewedProducts.visibility = View.GONE
        binding.rvBestProducts.visibility = View.GONE
        binding.rvCategories.visibility = View.GONE
    }

    fun setRecyclerViewsVisible(){
        binding.shimmerLayoutBestProducts.visibility = View.GONE
        binding.shimmerLayoutCategory?.visibility = View.GONE
        binding.shimmerLayoutMostViewed.visibility = View.GONE
        binding.shimmerLayoutNewest.visibility = View.GONE
        binding.rvNewestProducts.visibility = View.VISIBLE
        binding.rvMostViewedProducts.visibility = View.VISIBLE
        binding.rvBestProducts.visibility = View.VISIBLE
        binding.rvCategories.visibility = View.VISIBLE
    }
*/
}