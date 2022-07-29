package com.example.store.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentCategoriesBinding
import com.example.store.model.SharedFunctions
import com.example.store.ui.adapters.ProductListAdapter
import com.example.store.ui.utils.disableLoadingView
import com.example.store.ui.utils.enableLoadingView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CategoriesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesBinding
    private val categoriesViewModel: CategoriesViewModel by viewModels()
    private var productListAdapter: ProductListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            categoriesViewModel.categoryID=it.getInt("id")
            categoriesViewModel.findSpecificCategoryProduct(categoriesViewModel.categoryID)
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
        setProductListAdapter()
        observeLiveDatas(view)
        setButtonsListener()
    }

    private fun setProductListAdapter() {
        productListAdapter = ProductListAdapter {
            val action =
                CategoriesFragmentDirections.actionCategoriesFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.rvSpecificCategoryProducts.adapter = productListAdapter

    }

    private fun observeLiveDatas(view: View) {
        categoriesViewModel.specificCategoryProducts.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    setCategoryLayoutVisible()
                    disableLoadingView(binding.rvSpecificCategoryProducts, binding.loadingView)

                    response.data.let { list ->
                        productListAdapter!!.submitList(list)
                        try {
                            if (!list.isNullOrEmpty())
                                categoriesViewModel.findCategoryName(list[0])
                        } catch (e: Exception) {
                        }
                    }
                }
                is NetworkResult.Error -> {
                    setErrorLayoutVisible()
                    binding.loadingView.visibility = View.GONE
                    SharedFunctions.showSnackBar(response.message.toString(),view)
                }
                is NetworkResult.Loading -> {
                    setCategoryLayoutVisible()
                    enableLoadingView(binding.rvSpecificCategoryProducts, binding.loadingView)
                }
            }

        }

        categoriesViewModel.categoryName.observe(viewLifecycleOwner) {
            binding.tvCategoryName.text = it
        }

    }

    private fun setErrorLayoutVisible() {
        binding.llConnectionError.visibility = View.VISIBLE
        binding.llCategoryProducts.visibility = View.GONE
    }
    private fun setCategoryLayoutVisible() {
        binding.llConnectionError.visibility = View.GONE
        binding.llCategoryProducts.visibility = View.VISIBLE
    }

    private fun setButtonsListener() {
        binding.btnRetryCategoriesfrgmnt.setOnClickListener {
            categoriesViewModel.findSpecificCategoryProduct(categoriesViewModel.categoryID)
        }
    }


}