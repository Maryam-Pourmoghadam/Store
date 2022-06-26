package com.example.store.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentCategoriesBinding
import com.example.store.databinding.FragmentSearchBinding
import com.example.store.model.Status
import com.example.store.ui.adapters.ProductListAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    lateinit var binding: FragmentSearchBinding
    var ascDsc: String? = null
    var sortType: String? = null
    var categoryId: String? = null
    var color: String? = null
    var attribute: String? = null
    var attributeTerm: String? = null
    var size: String? = null
    private val searchViewModel: SearchViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val spinnerAsc = view.findViewById<Spinner>(R.id.spinner_asc_dsc)
        val spinnerSort = view.findViewById<Spinner>(R.id.spinner_sort)
        val spinnerCategory = view.findViewById<Spinner>(R.id.spinner_category)
        val spinnerColor = view.findViewById<Spinner>(R.id.spinner_color)
        val spinnerSize = view.findViewById<Spinner>(R.id.spinner_size)
        setSpinnersAdapters(spinnerAsc, spinnerSort, spinnerCategory, spinnerColor, spinnerSize)
        setSpinnersItemListeners(
            spinnerAsc,
            spinnerSort,
            spinnerCategory,
            spinnerColor,
            spinnerSize
        )


        binding.ibtnSearch.setOnClickListener {
            searchViewModel.searchProducts(
                binding.etSearch.text.toString(),
                categoryId,
                sortType,
                ascDsc,
                attribute,
                attributeTerm
            )
        }
        val searchListAdapter = ProductListAdapter {
            val action = SearchFragmentDirections.actionSearchFragmentToProductDetailsFragment(it)
            findNavController().navigate(action)
        }
        binding.rvSearchProducts.adapter = searchListAdapter
        searchViewModel.searchProductList.observe(viewLifecycleOwner) {
            if (it.isNullOrEmpty()){
                Snackbar.make(
                    view, "موردی یافت نشد",
                    Snackbar.LENGTH_LONG
                ).setAnimationMode(Snackbar.ANIMATION_MODE_FADE)
                    .show()
            }else {
                searchListAdapter.submitList(it)
            }
        }

        searchViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }
        binding.btnRetrySrchFragmnt.setOnClickListener {
            searchViewModel.searchProducts(
                binding.etSearch.text.toString(), categoryId,
                sortType,
                ascDsc, attribute, attributeTerm
            )
        }
    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llConnectionErrorS.visibility = View.VISIBLE
                binding.llSearchContent.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llConnectionErrorS.visibility = View.GONE
                binding.llSearchContent.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.VISIBLE

            }
            else -> {
                binding.llConnectionErrorS.visibility = View.GONE
                binding.llSearchContent.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun setSpinnersAdapters(
        spinnerAsc: Spinner,
        spinnerSort: Spinner,
        spinnerCategory: Spinner,
        spinnerColor: Spinner,
        spinnerSize: Spinner
    ) {
        val ascAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.asc_dsc,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        }
        spinnerAsc.adapter = ascAdapter

        val categoryAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.category,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerCategory.adapter = categoryAdapter

        val sortAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.sort,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerSort.adapter = sortAdapter

        val colorAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.color,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerColor.adapter = colorAdapter

        val sizeAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.size,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinnerSize.adapter = sizeAdapter

    }

    fun setSpinnersItemListeners(
        spinnerAsc: Spinner,
        spinnerSort: Spinner,
        spinnerCategory: Spinner,
        spinnerColor: Spinner,
        spinnerSize: Spinner
    ) {
        spinnerAsc.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    ascDsc = when (p2) {
                        1 -> "asc"
                        else -> null
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    ascDsc = null
                }
            }

        spinnerCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    categoryId = when (p2) {
                        1 -> "121"
                        2 -> "63"
                        3 -> "64"
                        4 -> "52"
                        5 -> "102"
                        6 -> "81"
                        7 -> "119"
                        8 -> "77"
                        9 -> "79"
                        10 -> "76"
                        else -> null
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    categoryId = null
                }

            }

        spinnerSort.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                sortType = when (p2) {
                    1 -> "popularity"
                    2 -> "price"
                    3 -> "date"
                    else -> {
                        ascDsc = null
                        spinnerAsc.setSelection(0)
                        null
                    }
                }

                spinnerAsc.isEnabled = !sortType.isNullOrEmpty()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                sortType = null
                ascDsc = null
                spinnerAsc.let {
                    it.isEnabled = false
                    it.setSelection(0)
                }
            }
        }

        spinnerColor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    attributeTerm = when (p2) {
                        1 -> "49"
                        2 -> "50"
                        3 -> "57"
                        4 -> "51"
                        5 -> "58"
                        6 -> "59"
                        else -> null
                    }
                    attribute = if (attributeTerm != null) {
                        "pa_color"
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    attributeTerm = null
                    attribute = null
                }
            }

        spinnerSize.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    attributeTerm = when (p2) {
                        1 -> "32"
                        2 -> "31"
                        3 -> "30"
                        4 -> "68"
                        5 -> "69"
                        else -> null
                    }
                    attribute = if (attributeTerm != null) {
                        "pa_size"
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    attributeTerm = null
                    attribute = null
                }
            }

    }
}