package com.example.store.ui.shoppingCart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentShoppingCartBinding
import com.example.store.model.CouponLines
import com.example.store.model.OrderItem
import com.example.store.model.ProductOrderItem
import com.example.store.model.Status
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {
    lateinit var binding: FragmentShoppingCartBinding
    var order: OrderItem? = null
    lateinit var orderListAdapter: OrderListAdapter
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

        orderListAdapter = OrderListAdapter({
            var tmpCount = it.quantity
            tmpCount++
            val modifiedList =
                shoppingCartViewModel.modifyOrderList(
                    it.productId,
                    tmpCount.toString()
                ) as MutableList
            setDataChangeInAdapter(modifiedList)
        }, {
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
            val modifiedList = shoppingCartViewModel.deleteSpecificOrder(it) as MutableList
            setDataChangeInAdapter(modifiedList)
        })

        binding.rvOrderList.adapter = orderListAdapter
        shoppingCartViewModel.orders.observe(viewLifecycleOwner) {
            orderListAdapter.submitList(it)

        }
        shoppingCartViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text = String.format("%.0f", it)
        }
        binding.btnCoupon?.setOnClickListener {
            shoppingCartViewModel.applyCoupon(binding.etCoupon?.text.toString(),requireContext())
        }

        binding.btnSendOrder.setOnClickListener {
            val customer = shoppingCartViewModel.getCustomerFromSharedPref(requireActivity())
            val orderedProducts =
                shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            if (orderedProducts.isNullOrEmpty()) {
                Toast.makeText(requireContext(), getString(R.string.shopping_cart_is_empty), Toast.LENGTH_SHORT).show()
            } else {
                if (customer == null) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.please_register_a_costumer),
                        Toast.LENGTH_SHORT
                    ).show()
                    findNavController().navigate(R.id.action_shoppingCartFragment_to_customerFragment)
                } else {
                    val couponList= mutableListOf<CouponLines>()
                    if (shoppingCartViewModel.couponCode.isNotEmpty())
                            couponList.add(CouponLines(shoppingCartViewModel.couponCode))
                    order = OrderItem(0, customer.id, orderedProducts, couponList)
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.wait_for_applying_order),
                        Toast.LENGTH_SHORT
                    ).show()
                    shoppingCartViewModel.sendOrders(order!!, requireContext(),orderListAdapter)
                    shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
                    binding.etCoupon?.setText("")
                }
            }
        }

        binding.btnRetryShoppingfrgmnt.setOnClickListener {
            shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            shoppingCartViewModel.sendOrders(order!!, requireContext(),orderListAdapter)
        }

        shoppingCartViewModel.orderResponse.observe(viewLifecycleOwner) {}
        shoppingCartViewModel.couponList.observe(viewLifecycleOwner){}

        shoppingCartViewModel.status.observe(viewLifecycleOwner) {
            setUIbyStatus(it)
        }
    }

    private fun setUIbyStatus(status: Status) {
        when (status) {
            Status.ERROR -> {
                binding.llErrorConnectionOrders.visibility = View.VISIBLE
                binding.llShoppingCartDetails.visibility = View.GONE
            }
            Status.LOADING -> {
                binding.llErrorConnectionOrders.visibility = View.GONE
                binding.llShoppingCartDetails.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.VISIBLE

            }
            else -> {
                binding.llErrorConnectionOrders.visibility = View.GONE
                binding.llShoppingCartDetails.visibility = View.VISIBLE
                // binding.shimmerLayout.visibility = View.GONE
            }
        }
    }

    private fun setDataChangeInAdapter(modifiedList: MutableList<ProductOrderItem>) {
        orderListAdapter.onCurrentListChanged(orderListAdapter.currentList, modifiedList)
        orderListAdapter.notifyDataSetChanged()
        shoppingCartViewModel.setModifiedListInSharedPref(modifiedList, requireActivity())
        shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
    }


}

/*
private fun createAlertDialog(modifiedOrderId: Int) {
    val view = layoutInflater.inflate(R.layout.dialog_edit_order, null)
    val editAlertDialog = android.app.AlertDialog.Builder(requireContext()).create()
    editAlertDialog.let {
        it.setTitle("ویرایش تعداد محصول")
        it.setCancelable(false)
        it.setMessage("عددی بین 0 تا 99 وارد کنید")
    }
    editAlertDialog.setButton(
        AlertDialog.BUTTON_POSITIVE, "ویرایش"
    ) { dialog, which ->
        val f: Dialog = dialog as Dialog
        //This is the input I can't get text from
        val etOrderCount = f.findViewById<EditText>(R.id.et_order_count)
        val tmpCount = etOrderCount.text.toString()
        if (isValidOrderCount(tmpCount)) {
            val modifiedList = shoppingCartViewModel.modifyOrderList(modifiedOrderId, tmpCount)
            orderListAdapter.submitList(modifiedList)
            shoppingCartViewModel.setModifiedListInSharedPref(modifiedList, requireActivity())
            shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            //Log.d("count : ","$count")
        } else {
            Toast.makeText(
                requireContext(),
                "ورودی نامعتبر است مجددا تلاش کنید",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    editAlertDialog.setButton(
        AlertDialog.BUTTON_NEGATIVE, "انصراف"
    ) { dialog, which -> editAlertDialog.dismiss() }

    editAlertDialog.setView(view)
    editAlertDialog.show()

}

private fun isValidOrderCount(count: String?): Boolean {
    if (count.isNullOrBlank())
        return false
    if (count.length > 2)
        return false
    if (count.length > 1 && count[0] == '0')
        return false

    return true
}*/


