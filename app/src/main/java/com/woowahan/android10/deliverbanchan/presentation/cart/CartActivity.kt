package com.woowahan.android10.deliverbanchan.presentation.cart

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityCartBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.CartDeliveryCompleteFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.recently.RecentlyViewedFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart, "CartActivity") {
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
                else -> RecentlyViewedFragment()
            }
        }else{
            when (tagArrayIndex) {
                0 -> fragment as CartMainFragment
                1 -> fragment as CartDeliveryCompleteFragment
                else -> fragment as RecentlyViewedFragment
            }
        }
        supportFragmentManager.commit {
            replace(R.id.cart_fcv, fragment, fragmentTagArray[tagArrayIndex])
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
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        cartViewModel.updateAllCartItemChanged()
    }
}
