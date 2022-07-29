package com.example.store.ui.products

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentProductsBinding
import com.example.store.model.SharedFunctions
import com.example.store.ui.adapters.ProductListAdapter
import com.example.store.ui.categories.CategoriesFragmentDirections
import com.example.store.ui.utils.disableLoadingView
import com.example.store.ui.utils.enableLoadingView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductsFragment : Fragment() {
    private lateinit var binding: FragmentProductsBinding
    private val productsViewModel: ProductsViewModel by viewModels()
    private lateinit var productListAdapter: ProductListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            productsViewModel.productsType = it.getString("type", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setProductListAdapter()
        initView()
        observeLiveDatas(view)
        setButtonListener()

    }

    private fun setButtonListener() {
        binding.btnRetryProductsfrgmnt.setOnClickListener {
            initView()
        }
    }

    private fun observeLiveDatas(view: View) {
        productsViewModel.productList.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    setProductLayoutVisible()
                    disableLoadingView(binding.rvProducts, binding.loadingView)

                    response.data.let { list ->
                        productListAdapter.submitList(list)
                    }
                }
                is NetworkResult.Error -> {
                    setErrorLayoutVisible()
                    binding.loadingView.visibility = View.GONE
                    SharedFunctions.showSnackBar(response.message.toString(), view)
                }
                is NetworkResult.Loading -> {
                    setProductLayoutVisible()
                    enableLoadingView(binding.rvProducts, binding.loadingView)


                }
            }

        }
    }

    private fun setProductListAdapter() {
        productListAdapter = ProductListAdapter {
            val action =
                ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.rvProducts.adapter = productListAdapter

    }

    private fun initView() {
        productsViewModel.getProducts(productsViewModel.productsType)
        binding.tvProductsType.text=productsViewModel.productsTypeName
    }

    private fun setErrorLayoutVisible() {
        binding.llConnectionError.visibility = View.VISIBLE
        binding.llProducts.visibility = View.GONE
    }
    private fun setProductLayoutVisible(){
        binding.llConnectionError.visibility = View.GONE
        binding.llProducts.visibility = View.VISIBLE
    }


}