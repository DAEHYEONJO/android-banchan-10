package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
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
import com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail.OrderDetailFragment
import com.woowahan.android10.deliverbanchan.presentation.state.UiLocalState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderListFragment :
    BaseFragment<FragmentOrderListBinding>(R.layout.fragment_order_list, "OrderListFragment") {

    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var orderListAdapter: OrderListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setRecyclerView()
        observeOrderListData()
    }

    private fun initView() {
        orderViewModel.currentFragmentIndex.value = 0
    }

    private fun setRecyclerView() {
        orderListAdapter = OrderListAdapter{
            orderViewModel.selectOrderListItem(it)
            // OrDetailFragment 로 이동 코드 추가하기
            orderViewModel.triggerMoveToOrderDetailFragmentEvent()
        }
        binding.orderRv.apply {
            adapter = orderListAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(OrderListVerticalDecoration(requireContext()))
        }
    }

    private fun observeOrderListData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.allOrderJoinState.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun <T> handleState(uiLocalState: UiLocalState<T>) {
        when (uiLocalState) {
            is UiLocalState.IsEmpty -> {
                binding.orderRv.toGone()
                binding.orderListTvEmptyMessage.toVisible()
            }
            is UiLocalState.IsLoading -> {
                binding.orderListPb.isVisible = uiLocalState.isLoading
            }
            is UiLocalState.ShowToast -> {
                requireContext().showToast(uiLocalState.message)
            }
            is UiLocalState.Success -> {
                binding.orderRv.toVisible()
                binding.orderListTvEmptyMessage.toGone()
                orderListAdapter.submitList(uiLocalState.uiDishItems as List<UiOrderListItem>)
            }
            is UiLocalState.Error -> {}
        }
    }
}