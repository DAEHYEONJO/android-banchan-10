package com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentOrderDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.order.OrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>(
    R.layout.fragment_order_detail, "OrderDetailFragment"
) {

    private val orderViewModel: OrderViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        orderViewModel.currentFragmentName.value = "OrderDetail"
    }
}