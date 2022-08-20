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
import com.woowahan.android10.deliverbanchan.presentation.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(
    R.layout.fragment_order_detail, "OrderDetailFragment"
) {

    private val orderViewModel: OrderViewModel by activityViewModels()
    private lateinit var orderDetailAdapter: OrderDetailAdapter
    private lateinit var orderInfoAdapter: OrderInfoAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setRecyclerView()
        observeData()
    }

    private fun initView() {
        orderViewModel.currentFragmentName.value = "OrderDetail"
    }

    private fun setRecyclerView() {
        orderDetailAdapter = OrderDetailAdapter()
        orderInfoAdapter = OrderInfoAdapter()
        concatAdapter =
            ConcatAdapter(orderDetailAdapter, orderInfoAdapter)

        binding.orderDetailRv.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.selectedOrderList.collect {
                    orderDetailAdapter.submitList(it.toList())
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.selectedOrderInfo.collect {
                    orderInfoAdapter.submitList(listOf(it))
                }
            }
        }
    }
}