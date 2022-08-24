package com.woowahan.android10.deliverbanchan.presentation.order

import android.os.Bundle
import android.util.Log
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
    private val fragmentTagArray: Array<String> by lazy {
        resources.getStringArray(R.array.order_fragment_tag_array)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBroadcastOrderDetailIntent()
        initBinding()
        initAppBar()
        initObserver()
    }

    private fun initBroadcastOrderDetailIntent() {
        Log.e(TAG, "initBroadcastOrderDetailIntent: ${intent.getLongExtra("orderTimeStamp", 0L)}")
        val timeStamp = intent.getLongExtra("orderTimeStamp", 0L)
        orderViewModel.setNotificationExtraTimeStamp(timeStamp)
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
            appBarWithBackBtnIvReload.setClickEventWithDuration(
                duration = 1000,
                coroutineScope = lifecycleScope
            ) {
                appBarWithBackBtnIvReload.startAnimation(rotateAnimation)
                orderViewModel.setReloadBtnValue()
            }
        }
    }

    private fun initObserver() {
        orderViewModel.currentFragmentIndex.observe(this@OrderActivity) { tagArrayIndex ->
            initFragment(tagArrayIndex)
            when (tagArrayIndex) {
                0 -> {
                    setOrderListAppBar()
                }
                1 -> {
                    setOrderDetailAppBar()
                }
            }
        }
    }

    private fun initFragment(tagArrayIndex: Int) {
        with(supportFragmentManager){
            var fragment = findFragmentByTag(fragmentTagArray[tagArrayIndex])
            if (fragment == null) {
                fragment = when (tagArrayIndex) {
                    0 -> OrderListFragment()
                    else -> OrderDetailFragment()
                }
            } else {
                when (tagArrayIndex) {
                    0 -> fragment as OrderListFragment
                    else -> fragment as OrderDetailFragment
                }
            }
            if (orderViewModel.fromNotificationExtraTimeStamp.value != 0L) { // 알림을 통해 온 경우
                commit {
                    replace(R.id.order_fcv, fragment, fragmentTagArray[tagArrayIndex])
                }
            } else {
                commit {
                    replace(R.id.order_fcv, fragment, fragmentTagArray[tagArrayIndex])
                    if (tagArrayIndex != 0 && orderViewModel.fromNotificationExtraTimeStamp.value == 0L) {
                        // ListFragment 를 띄워야 하는 경우가 아니라면 백스택에 추가해주기
                        if (backStackEntryCount==0) addToBackStack("OrderBackStack")
                    }
                }
            }
        }
    }

    private fun setOrderListAppBar() {
        with(orderViewModel){
            setAppBarTitle(resources.getString(R.string.app_bar_order_list_title))
            orderDetailMode.value = false
        }
        binding.orderAbl.appBarWithBackBtnIvReload.toGone()
    }

    private fun setOrderDetailAppBar() {
        with(orderViewModel){
            setAppBarTitle("")
            orderDetailMode.value = true
        }
        binding.orderAbl.appBarWithBackBtnIvReload.toVisible()
    }

    override fun onBackPressed() {
        with(orderViewModel){
            if (currentFragmentIndex.value == 0) super.onBackPressed()
            else if (fromNotificationExtraTimeStamp.value==0L){
                supportFragmentManager.popBackStack()
                setFragmentIndex(0)
            }else{
                super.onBackPressed()
            }
        }
    }

}