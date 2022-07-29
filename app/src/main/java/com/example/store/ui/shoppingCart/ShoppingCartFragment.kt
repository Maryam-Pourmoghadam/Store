package com.example.store.ui.shoppingCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.data.network.NetworkResult
import com.example.store.databinding.FragmentShoppingCartBinding
import com.example.store.model.CouponLines
import com.example.store.model.OrderItem
import com.example.store.model.ProductOrderItem
import com.example.store.model.SharedFunctions
import com.example.store.ui.utils.disableLoadingView
import com.example.store.ui.utils.enableLoadingView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {
    private lateinit var binding: FragmentShoppingCartBinding
    private var order: OrderItem? = null
    private lateinit var orderListAdapter: OrderListAdapter
    private val shoppingCartViewModel: ShoppingCartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShoppingCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()
        observeLiveDatas(view)
        setButtonsListener(view)


    }

    private fun setAdapter() {
        orderListAdapter = OrderListAdapter({
            //plus listener
            var tmpCount = it.quantity
            tmpCount++
            val modifiedList =
                shoppingCartViewModel.modifyOrderList(
                    it.productId,
                    tmpCount.toString()
                ) as MutableList
            setDataChangeInAdapter(modifiedList)
        }, {
            //minus listener
            var tmpCount = it.quantity
            if (tmpCount > 1) {
                tmpCount--
                val modifiedList =
                    shoppingCartViewModel.modifyOrderList(
                        it.productId,
                        tmpCount.toString()
                    ) as MutableList
                setDataChangeInAdapter(modifiedList)
            }
        }, {
            //delete listener
            val modifiedList = shoppingCartViewModel.deleteSpecificOrder(it) as MutableList
            setDataChangeInAdapter(modifiedList)
        })

        binding.rvOrderList.adapter = orderListAdapter

    }

    private fun setButtonsListener(view: View) {
        binding.btnCoupon.setOnClickListener {
            shoppingCartViewModel.applyCoupon(binding.etCoupon.text.toString(), requireActivity())
        }

        binding.btnSendOrder.setOnClickListener {
            val customer = shoppingCartViewModel.getCustomerFromSharedPref(requireActivity())
            val orderedProducts =
                shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            if (orderedProducts.isNullOrEmpty()) {
                SharedFunctions.showSnackBar(getString(R.string.shopping_cart_is_empty), view)
            } else {
                if (customer == null) {
                    SharedFunctions.showSnackBar(
                        getString(R.string.please_register_a_costumer),
                        view
                    )
                    findNavController().navigate(R.id.action_shoppingCartFragment_to_customerFragment)
                } else {
                    val couponList = mutableListOf<CouponLines>()
                    if (shoppingCartViewModel.couponCode.isNotEmpty())
                        couponList.add(CouponLines(shoppingCartViewModel.couponCode))
                    order = OrderItem(0, customer.id, orderedProducts, couponList)
                    shoppingCartViewModel.sendOrders(order!!)
                }
            }
        }

    }

    private fun observeLiveDatas(view: View) {
        shoppingCartViewModel.orders.observe(viewLifecycleOwner) {
            orderListAdapter.submitList(it)
        }
        shoppingCartViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = String.format("%.0f", it)
        }

        shoppingCartViewModel.orderResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is NetworkResult.Success -> {
                    disableLoadingView(binding.rvOrderList,binding.loadingView)
                    setButtonsEnable()
                    SharedFunctions.showSnackBar("سفارش شما با موفقیت ثبت شد", view)
                    shoppingCartViewModel.emptyOrderList(requireContext())
                    val modifiedList =
                        shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
                    orderListAdapter.submitList(modifiedList)
                    binding.etCoupon.setText("")
                }
                is NetworkResult.Error -> {
                    disableLoadingView(binding.rvOrderList,binding.loadingView)
                    setButtonsEnable()
                    SharedFunctions.showSnackBar(
                        response.message.toString() + "  در حال حاضر ارسال سفارش امکان پذیر نیست",
                        view
                    )
                }
                is NetworkResult.Loading -> {
                    enableLoadingView(binding.rvOrderList,binding.loadingView)
                    setButtonsDisable()
                    SharedFunctions.showSnackBar("جهت ثبت سفارش منتظر بمانید", view)
                }
            }
        }

        shoppingCartViewModel.couponList.observe(viewLifecycleOwner) { response ->

            if (response is NetworkResult.Error) {
                SharedFunctions.showSnackBar(
                    response.message.toString() + " متاسفانه کدهای تخفیف درحال حاضر قابل اعمال نیست",
                    view
                )
            }
        }
    }

    private fun setDataChangeInAdapter(modifiedList: MutableList<ProductOrderItem>) {
        orderListAdapter.onCurrentListChanged(orderListAdapter.currentList, modifiedList)
        orderListAdapter.notifyDataSetChanged()
        shoppingCartViewModel.setModifiedListInSharedPref(modifiedList, requireActivity())
        shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
    }

    private fun setButtonsDisable()
    {
        binding.btnSendOrder.isEnabled=false
        binding.btnCoupon.isEnabled=false
    }
    private fun setButtonsEnable()
    {
        binding.btnSendOrder.isEnabled=true
        binding.btnCoupon.isEnabled=true
    }


    override fun onDestroy() {
        super.onDestroy()
        shoppingCartViewModel.orderResponse.value = null
    }


}



