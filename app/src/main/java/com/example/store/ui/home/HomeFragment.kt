package com.example.store.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentHomeBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.CategoryListadapter
import com.example.store.ui.adapters.ProductListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    lateinit var binding: FragmentHomeBinding
    val homeViewModel: HomeViewModel  by viewModels()
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
            mostViewedProductsListAdapter.submitList(list.sortedByDescending { it.ratingCount })
            bestProductsListAdapter.submitList(list.sortedByDescending { it.averageRating })

        }
        homeViewModel.categoryList.observe(viewLifecycleOwner) {
            categoryListadapter.submitList(it)

        }

        homeViewModel.status.observe(viewLifecycleOwner) {
            if (it == Status.ERROR)
                Snackbar.make(
                    view, R.string.network_error,
                    Snackbar.LENGTH_SHORT
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()

        }

    }

    private fun navigateToDetailsFragment(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(id)
        findNavController().navigate(action)
    }

}