package com.woowahan.android10.deliverbanchan.presentation.cart.complete

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDeliveryCompleteBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.CartDeliveryBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.CartDeliveryFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.CartDeliveryTopAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartDeliveryCompleteFragment : BaseFragment<FragmentCartDeliveryCompleteBinding>(
    R.layout.fragment_cart_delivery_complete,
    "CartDeliveryCompleteFragment"
) {

    @Inject lateinit var cartDishCompleteTopAdapter: CartDeliveryTopAdapter
    @Inject lateinit var cartDishCompleteBodyAdapter: CartDeliveryBodyAdapter
    @Inject lateinit var cartDishCompleteFooterAdapter: CartDeliveryFooterAdapter
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(cartDishCompleteTopAdapter, cartDishCompleteBodyAdapter, cartDishCompleteFooterAdapter)
    }
    private val cartViewModel: CartViewModel by activityViewModels ()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()
    }

    private fun initObserver() {
        with(cartViewModel){
            orderCompleteTopItem.observe(viewLifecycleOwner){
                cartDishCompleteTopAdapter.cartDeliveryTopList = listOf(it)
                cartDishCompleteTopAdapter.notifyDataSetChanged()
            }
            orderCompleteBodyItem.observe(viewLifecycleOwner){
                cartDishCompleteBodyAdapter.cartDeliveryTopList = it
                cartDishCompleteBodyAdapter.notifyDataSetChanged()
            }
            orderCompleteFooterItem.observe(viewLifecycleOwner){
                cartDishCompleteFooterAdapter.cartDeliveryBottomList = listOf(it)
                cartDishCompleteFooterAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun initRecyclerView() {
        binding.cartDeliveryCompleteRv.adapter = concatAdapter
        binding.cartDeliveryCompleteRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}