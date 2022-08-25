package com.woowahan.android10.deliverbanchan.presentation.cart

import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.work.WorkManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.background.getOneTimeRequestBuilder
import com.woowahan.android10.deliverbanchan.databinding.ActivityCartBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.CartDeliveryCompleteFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.RecentViewedFragment
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.NumberDialogFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart, "CartActivity"), NumberDialogFragment.OnNumberDialogClickListener{

    companion object{
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
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initAppBar()
        initObserver()
    }

    private fun initObserver() {
        cartViewModel.fragmentArrayIndex.observe(this){ fragmentArrayIndex ->
            initFragment(fragmentArrayIndex)
        }
    }

    private fun initFragment(tagArrayIndex: Int) {
        var fragment = supportFragmentManager.findFragmentByTag(fragmentTagArray[tagArrayIndex])
        if (fragment == null){
            fragment = when(tagArrayIndex){
                0 -> CartMainFragment()
                1 -> CartDeliveryCompleteFragment()
                else -> RecentViewedFragment()
            }
        }else{
            when (tagArrayIndex) {
                0 -> fragment as CartMainFragment
                1 -> fragment as CartDeliveryCompleteFragment
                else -> fragment as RecentViewedFragment
            }
        }
        supportFragmentManager.commit {
            replace(R.id.cart_fcv, fragment, fragmentTagArray[tagArrayIndex])
            if (tagArrayIndex == 2 && supportFragmentManager.backStackEntryCount==0){
                addToBackStack(BACKSTACK_TAG)
            }
        }
    }

    private fun initBinding() {
        with(binding){
            lifecycleOwner = this@CartActivity
            vm = cartViewModel
        }
    }

    private fun initAppBar() {
        cartViewModel.setAppBarTitle(resources.getString(R.string.app_bar_cart_title))
        with(binding.cartAbl){

            appBarWithBackBtnIvLeft.setOnClickListener {
                when(cartViewModel.fragmentArrayIndex.value){
                    2 -> {
                       supportFragmentManager.popBackStack()
                        cartViewModel.fragmentArrayIndex.value = 0
                    }else -> onBackPressed()
                }
            }

            appBarWithBackBtnIvReload.setClickEventWithDuration(duration = 1000, coroutineScope = lifecycleScope){
                appBarWithBackBtnIvReload.startAnimation(rotateAnimation)
                cartViewModel.setReloadBtnValue()
            }

        }
    }

    override fun onStop() {
        super.onStop()
        cartViewModel.updateAllCartItemChanged()
    }

}
