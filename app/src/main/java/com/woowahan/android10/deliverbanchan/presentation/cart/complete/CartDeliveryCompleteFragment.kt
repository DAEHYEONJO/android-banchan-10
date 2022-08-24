package com.woowahan.android10.deliverbanchan.presentation.cart.complete

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDeliveryCompleteBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryTopAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class CartDeliveryCompleteFragment : BaseFragment<FragmentCartDeliveryCompleteBinding>(
    R.layout.fragment_cart_delivery_complete,
    "CartDeliveryCompleteFragment"
) {

    @Inject lateinit var cartDishCompleteTopAdapter: DeliveryTopAdapter
    @Inject lateinit var cartDishCompleteBodyAdapter: DeliveryBodyAdapter
    @Inject lateinit var cartDishCompleteFooterAdapter: DeliveryFooterAdapter
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
            
            orderCompleteTopItem.flowWithLifecycle(lifecycle).onEach{
                Log.e(TAG, "initObserver: 화면회점됨", )
                cartDishCompleteTopAdapter.cartDeliveryTopList = listOf(it)
                cartDishCompleteTopAdapter.notifyDataSetChanged()
            }.launchIn(lifecycleScope)
            
            orderCompleteBodyItem.flowWithLifecycle(lifecycle).onEach{
                cartDishCompleteBodyAdapter.cartDeliveryTopList = it
                cartDishCompleteBodyAdapter.notifyDataSetChanged()
            }.launchIn(lifecycleScope)
            
            orderCompleteFooterItem.flowWithLifecycle(lifecycle).onEach{
                cartDishCompleteFooterAdapter.cartDeliveryBottomList = listOf(it)
                cartDishCompleteFooterAdapter.notifyDataSetChanged()
            }.launchIn(lifecycleScope)
            
        }
    }

    private fun initRecyclerView() {
        binding.cartDeliveryCompleteRv.adapter = concatAdapter
        binding.cartDeliveryCompleteRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
    }
}