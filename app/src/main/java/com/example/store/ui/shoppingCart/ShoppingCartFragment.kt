package com.example.store.ui.shoppingCart

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.store.R
import com.example.store.databinding.FragmentShoppingCartBinding
import com.example.store.ui.adapters.OrderListAdapter
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ShoppingCartFragment : Fragment() {
    lateinit var binding: FragmentShoppingCartBinding
    var count: String? = null
    var modifiedOrderId = -1
    lateinit var orderListAdapter: OrderListAdapter
    val shoppingCartViewModel: ShoppingCartViewModel by viewModels()
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

        orderListAdapter = OrderListAdapter {
            createAlertDialog(it)
        }
        binding.rvOrderList.adapter = orderListAdapter
        shoppingCartViewModel.orders.observe(viewLifecycleOwner) {
            //shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            orderListAdapter.submitList(it)

        }
        shoppingCartViewModel.totalPrice.observe(viewLifecycleOwner) {
            binding.tvTotalPrice.text=String.format("%.0f", it)
        }

        binding.btnSendOrder.setOnClickListener {
            if (shoppingCartViewModel.getCustomerFromSharedPref(requireActivity())==null){
                Toast.makeText(requireContext(),"لطفا ابتدا یک مشتری رجیستر کنید",Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_shoppingCartFragment_to_customerFragment)
            }else{
                if (shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())!=null) {
                    Toast.makeText(requireContext(), "سفارش شما ثبت شد", Toast.LENGTH_SHORT).show()
                    shoppingCartViewModel.clearOrderList(requireContext())
                }else{
                    Toast.makeText(requireContext(), "سبد خريد خالي است", Toast.LENGTH_SHORT).show()
                }

                shoppingCartViewModel.getOrderedProductsFromSharedPref(requireActivity())
            }
        }


    }


    fun createAlertDialog(modifiedOrderId:Int) {
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
               val modifiedList= shoppingCartViewModel.modifyOrderList(modifiedOrderId, tmpCount)
                orderListAdapter.submitList(modifiedList)
                shoppingCartViewModel.setModifiedListInSharedPref(modifiedList,requireActivity())
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

    fun isValidOrderCount(count: String?): Boolean {
        if (count.isNullOrBlank())
            return false
        if (count.length > 2)
            return false
        if (count.length > 1 && count[0] == '0')
            return false

        return true
    }

}