package com.woowahan.android10.deliverbanchan.presentation.cart

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.commit
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityCartBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart, "CartActivity") {
    private val cartViewModel: CartViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initAppBar()
        initFragment()
        initObserver()
    }

    private fun initObserver() {

    }

    private fun initFragment() {
        var fragment = supportFragmentManager.findFragmentByTag("CartMainFragment")
        if (fragment == null){
            fragment = CartMainFragment()
        }else{
            fragment as CartMainFragment
        }
        supportFragmentManager.commit {
            replace(R.id.cart_fcv, fragment, "CartMainFragment")
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
}
