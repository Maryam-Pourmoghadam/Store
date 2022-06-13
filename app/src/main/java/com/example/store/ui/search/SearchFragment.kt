package com.example.store.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCategoriesBinding
import com.example.store.databinding.FragmentSearchBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.ProductListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
val searchViewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding= FragmentSearchBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ibtnSearch.setOnClickListener {
            searchViewModel.searchProducts(binding.etSearch.text.toString())
        }
        val searchListAdapter=ProductListAdapter{
            val action=SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.rvSearchProducts.adapter=searchListAdapter
        searchViewModel.searchProductList.observe(viewLifecycleOwner){
            searchListAdapter.submitList(it)
        }
        searchViewModel.status.observe(viewLifecycleOwner){
            setUIbyStatus(it)
        }
        binding.btnRetrySrchFragmnt.setOnClickListener {
            searchViewModel.searchProducts(binding.etSearch.text.toString())
        }
    }
    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llConnectionErrorS.visibility = View.VISIBLE
                binding.llSearchContent?.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llConnectionErrorS.visibility = View.GONE
                binding.llSearchContent?.visibility = View.VISIBLE
               // binding.shimmerLayout.visibility = View.VISIBLE

            }
            else -> {
                binding.llConnectionErrorS.visibility = View.GONE
                binding.llSearchContent?.visibility = View.VISIBLE
               // binding.shimmerLayout.visibility = View.GONE
            }
        }
    }
}