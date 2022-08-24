package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderListBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiOrderListItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.OrderListVerticalDecoration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.order.OrderViewModel
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

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
            Log.e(TAG, "setRecyclerView: 클릭", )
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
            }.launchIn(lifecycleScope)
        }
    }

    private fun <T> handleState(uiLocalState: UiLocalState<T>) {
        when (uiLocalState) {
            is UiLocalState.Empty -> {
                binding.orderRv.toGone()
                binding.orderListTvEmptyMessage.toVisible()
            }
            is UiLocalState.Loading -> {
                binding.orderListPb.isVisible = uiLocalState.isLoading
            }
            is UiLocalState.ShowToast -> {
                requireContext().showToast(uiLocalState.message)
            }
            is UiLocalState.Success -> {
                binding.orderRv.toVisible()
                binding.orderListTvEmptyMessage.toGone()
                val uiOrderList = uiLocalState.uiDishItems as List<UiOrderListItem>
                if (orderViewModel.fromNotificationExtraTimeStamp.value != 0L) {
                    orderViewModel.selectOrderListItem(uiOrderList.find { it.timeStamp==orderViewModel.fromNotificationExtraTimeStamp.value }!!.orderList)
                    orderViewModel.setFragmentIndex(1)
                }else{
                    orderListAdapter.submitList(uiOrderList)
                }
            }
            is UiLocalState.Error -> {}
        }
    }
}