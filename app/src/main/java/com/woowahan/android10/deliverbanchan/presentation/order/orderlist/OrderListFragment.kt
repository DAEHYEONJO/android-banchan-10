package com.woowahan.android10.deliverbanchan.presentation.order.orderlist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderListBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderListFragment :
    BaseFragment<FragmentOrderListBinding>(R.layout.fragment_order_list, "OrderListFragment") {

    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        orderViewModel.currentFragmentName.value = "OrderList"
    }
}