package com.example.store.ui.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCategoriesBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.CategoryListadapter
import com.example.store.ui.adapters.ProductListAdapter
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel


class CategoriesFragment : Fragment() {
    lateinit var binding: FragmentCategoriesBinding
    val categoriesViewModel: CategoriesViewModel by viewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            val categoryID = it.getInt("id")
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
        var isConnected = false
        val productListAdapter = ProductListAdapter {
            if (isConnected) {
                val action =
                    CategoriesFragmentDirections.actionCategoriesFragmentToProductDetailsFragment(it)
                findNavController().navigate(action)
            }
        }
        binding.rvSpecificCategoryProducts.adapter = productListAdapter

        categoriesViewModel.specificCategoryProducts.observe(viewLifecycleOwner) {

            productListAdapter.submitList(it)
            if (isConnected)
                categoriesViewModel.findCategoryName(it[0])
        }

        categoriesViewModel.categoryName.observe(viewLifecycleOwner) {
            binding.tvCategoryName.text = it
        }

        categoriesViewModel.status.observe(viewLifecycleOwner) {
            isConnected = it != Status.ERROR
            if (it == Status.ERROR)
                Snackbar.make(
                    view, "network error",
                    Snackbar.LENGTH_SHORT
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE).show()

        }
    }
}