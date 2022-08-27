package com.woowahan.android10.deliverbanchan.presentation.cart.host

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityCartBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.CartDeliveryCompleteFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.RecentViewedFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.viewmodel.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.NumberDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart, "CartActivity"),
    NumberDialogFragment.OnNumberDialogClickListener {

    companion object {
        const val BACKSTACK_TAG = "CartBackStack"
    }

    private val rotateAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(this@CartActivity, R.anim.rotate_degree_360)
    }

    override fun onClickAmountChangeBtn(position: Int, amount: Int) {
        cartViewModel.updateUiCartAmountValue(position, amount)
    }

    private val cartViewModel: CartViewModel by viewModels()
    private val fragmentTagArray: Array<String> by lazy {
        resources.getStringArray(R.array.cart_fragment_tag_array)
    }// cartMain, orderComplete, recent
    private val fragmentAppBarTitleArray: Array<String> by lazy {
        resources.getStringArray(R.array.cart_fragment_app_bar_title_array)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initAppBar()
        initObserver()
    }

    private fun initObserver() {
        cartViewModel.fragmentArrayIndex.observe(this) { fragmentArrayIndex ->
            initFragment(fragmentArrayIndex)
        }
    }

    private fun initFragment(tagArrayIndex: Int) {
        var fragment = supportFragmentManager.findFragmentByTag(fragmentTagArray[tagArrayIndex])
        if (fragment == null) {
            fragment = when (tagArrayIndex) {
                0 -> CartMainFragment()
                1 -> CartDeliveryCompleteFragment()
                else -> RecentViewedFragment()
            }
        } else {
            when (tagArrayIndex) {
                0 -> fragment as CartMainFragment
                1 -> fragment as CartDeliveryCompleteFragment
                else -> fragment as RecentViewedFragment
            }
        }
        supportFragmentManager.commit {
            replace(R.id.cart_fcv, fragment, fragmentTagArray[tagArrayIndex])
            if (tagArrayIndex == 2 && supportFragmentManager.backStackEntryCount == 0) {
                addToBackStack(BACKSTACK_TAG)
            }
        }
        cartViewModel.setAppBarTitle(fragmentAppBarTitleArray[cartViewModel.fragmentArrayIndex.value!!])
    }

    private fun initBinding() {
        with(binding) {
            lifecycleOwner = this@CartActivity
            vm = cartViewModel
        }
    }

    private fun initAppBar() {
        with(binding.cartAbl) {
            appBarWithBackBtnIvLeft.setOnClickListener {
                onBackPressed()
            }
            appBarWithBackBtnIvReload.setClickEventWithDuration(
                duration = 1000,
                coroutineScope = lifecycleScope
            ) {
                appBarWithBackBtnIvReload.startAnimation(rotateAnimation)
                cartViewModel.setReloadBtnValue()
            }

        }
    }

    override fun onStop() {
        super.onStop()
        cartViewModel.updateAllCartItemChanged()
    }

    override fun onBackPressed() {
        with(cartViewModel.fragmentArrayIndex.value!!) {
            cartViewModel.setAppBarTitle(fragmentAppBarTitleArray[this])
            when (this) {
                2 -> {
                    supportFragmentManager.popBackStack()
                    cartViewModel.fragmentArrayIndex.value = 0
                }
                else -> {
                    super.onBackPressed()
                }
            }
        }
    }

}
