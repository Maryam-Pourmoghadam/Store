package com.example.store.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCategoriesBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.ProductListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    val categoriesViewModel: CategoriesViewModel by viewModels()
    var categoryID = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoryID = it.getInt("id")
            categoriesViewModel.findSpecificCategoryProduct(categoryID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productListAdapter = ProductListAdapter {
            val action =
                CategoriesFragmentDirections.actionCategoriesFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }

        binding.rvSpecificCategoryProducts.adapter = productListAdapter

        categoriesViewModel.specificCategoryProducts.observe(viewLifecycleOwner) {
            productListAdapter.submitList(it)
            try {
                categoriesViewModel.findCategoryName(it[0])
            } catch (e: Exception) {
            }
        }

        categoriesViewModel.categoryName.observe(viewLifecycleOwner) {
            binding.tvCategoryName.text = it
        }

        categoriesViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }


        binding.btnRetryCategoriesfrgmnt.setOnClickListener {
            categoriesViewModel.findSpecificCategoryProduct(categoryID)
        }

    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llConnectionError.visibility = View.VISIBLE
                binding.llCategoryProducts.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llConnectionError.visibility = View.GONE
                binding.llCategoryProducts.visibility = View.VISIBLE
                 binding.shimmerLayoutCategory.visibility = View.VISIBLE
                binding.rvSpecificCategoryProducts.visibility=View.GONE
            }
            else -> {
                binding.llConnectionError.visibility = View.GONE
                binding.llCategoryProducts.visibility = View.VISIBLE
                binding.shimmerLayoutCategory.visibility = View.GONE
                binding.rvSpecificCategoryProducts.visibility=View.VISIBLE
            }
        }
    }
}