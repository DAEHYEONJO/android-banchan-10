package com.woowahan.android10.deliverbanchan.presentation.order

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityOrderBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.order.orderlist.OrderListFragment
import com.woowahan.android10.deliverbanchan.presentation.order.orderlistdetail.OrderDetailFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OrderActivity : BaseActivity<ActivityOrderBinding>(R.layout.activity_order, "OrderActivity") {

    private val orderViewModel: OrderViewModel by viewModels()
    private val rotateAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this@OrderActivity, R.anim.rotate_degree_360)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBinding()
        initAppBar()
        initFragment()
        observeFragment()
        observeMoveEvent()
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
                onBackPressed()
            }
            appBarWithBackBtnIvReload.setClickEventWithDuration(duration = 1000, coroutineScope = lifecycleScope){
                appBarWithBackBtnIvReload.startAnimation(rotateAnimation)
                orderViewModel.setReloadBtnValue()
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
                orderViewModel.currentFragmentIndex.collect {
                    when (it) {
                        0 -> setOrderListAppBar()
                        1 -> setOrderDetailAppBar()
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

    private fun observeMoveEvent() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                orderViewModel.moveToOrderDetailEvent.collect {
                    if (it) {
                        supportFragmentManager.commit {
                            replace(R.id.order_fcv, OrderDetailFragment(), "OrderDetail")
                            addToBackStack("OrderDetail")
                        }
                    }
                }
            }
        }
    }
}