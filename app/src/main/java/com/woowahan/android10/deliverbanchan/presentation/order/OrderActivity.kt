package com.woowahan.android10.deliverbanchan.presentation.order

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityOrderBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.order.orderlist.OrderListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>(R.layout.activity_order, "OrderActivity") {

    private val orderViewModel: OrderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initAppBar()
        initFragment()
        observeFragment()
    }

    private fun initBinding() {
        with(binding) {
            lifecycleOwner = this@OrderActivity
            vm = orderViewModel
        }
    }

    private fun initAppBar() {
        orderViewModel.setAppBarTitle(resources.getString(R.string.app_bar_order_list_title))
        with(binding.orderAbl) {
            appBarWithBackBtnIvLeft.setOnClickListener {
                finish()
            }
        }
    }

    private fun initFragment() {
        supportFragmentManager.commit {
            replace(R.id.order_fcv, OrderListFragment())
        }
    }

    private fun observeFragment() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.currentFragmentName.collect {
                    when (it) {
                        "OrderList" -> setOrderListAppBar()
                        "OrderDetail" -> setOrderDetailAppBar()
                    }
                }
            }
        }
    }

    private fun setOrderListAppBar() {
        orderViewModel.setAppBarTitle(resources.getString(R.string.app_bar_order_list_title))
        orderViewModel.orderDetailMode.value = false
        binding.orderAbl.appBarWithBackBtnIvReload.toGone()
    }

    private fun setOrderDetailAppBar() {
        orderViewModel.setAppBarTitle("")
        orderViewModel.orderDetailMode.value = true
        binding.orderAbl.appBarWithBackBtnIvReload.toVisible()
    }
}