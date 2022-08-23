package com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryBodyAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryFooterAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.adapter.DeliveryTopAdapter
import com.woowahan.android10.deliverbanchan.presentation.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(
    R.layout.fragment_order_detail, "OrderDetailFragment"
) {

    private val orderViewModel: OrderViewModel by activityViewModels()
    @Inject
    lateinit var orderDetailTopAdapter: DeliveryTopAdapter
    @Inject
    lateinit var orderDetailBodyAdapter: DeliveryBodyAdapter
    @Inject
    lateinit var orderDetailFooterAdapter: DeliveryFooterAdapter
    private val concatAdapter: ConcatAdapter by lazy {
        ConcatAdapter(orderDetailBodyAdapter, orderDetailBodyAdapter, orderDetailFooterAdapter)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setRecyclerView()
        observeData()
    }

    private fun initView() {
        orderViewModel.currentFragmentIndex.value = 1
    }

    private fun setRecyclerView() {
        binding.orderDetailRv.apply {
            adapter = concatAdapter

        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.selectedOrderList.collect {
                    orderDetailBodyAdapter.cartDeliveryTopList = it
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.selectedOrderInfo.collect {
                    orderDetailFooterAdapter.cartDeliveryBottomList = listOf(it)
                }
            }
        }
    }
}