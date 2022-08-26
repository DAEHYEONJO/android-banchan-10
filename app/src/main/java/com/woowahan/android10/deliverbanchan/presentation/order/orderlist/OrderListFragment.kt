package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderListBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.OrderListVerticalDecoration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.order.adapter.OrderListAdapter
import com.woowahan.android10.deliverbanchan.presentation.order.viewmodel.OrderViewModel
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class OrderListFragment :
    BaseFragment<FragmentOrderListBinding>(R.layout.fragment_order_list, "OrderListFragment") {

    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerView()
        observeOrderListData()
    }


    private fun setRecyclerView() {
        orderListAdapter = OrderListAdapter {
            orderViewModel.setFragmentIndex(1)
            orderViewModel.selectOrderListItem(it)
        }
        binding.orderRv.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(OrderListVerticalDecoration(requireContext()))
        }
    }

    private fun observeOrderListData() {
        with(orderViewModel) {
            allOrderJoinState.flowWithLifecycle(lifecycle).onEach {
                handleState(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun <T> handleState(uiState: UiState<T>) {
        when (uiState) {
            is UiState.Empty -> {
                binding.orderRv.toGone()
                binding.orderListTvEmptyMessage.toVisible()
            }
            is UiState.Loading -> {
                binding.orderListPb.isVisible = uiState.isLoading
            }
            is UiState.Success -> {
                binding.orderRv.toVisible()
                binding.orderListTvEmptyMessage.toGone()
                val uiOrderList = uiState.items as List<UiOrderListItem>
                if (orderViewModel.fromNotificationExtraTimeStamp.value != 0L) {
                    orderViewModel.selectOrderListItem(uiOrderList.find { it.timeStamp == orderViewModel.fromNotificationExtraTimeStamp.value }!!.orderList)
                    orderViewModel.setFragmentIndex(1)
                } else {
                    orderListAdapter.submitList(uiOrderList)
                }
            }
            is UiState.Error -> {}
        }
    }
}