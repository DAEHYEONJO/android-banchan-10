package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.host.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.order.host.OrderActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity"){

    private val dishViewModel: DishViewModel by viewModels()
    private lateinit var tabTitleArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initBtn()
    }

    @OptIn(FlowPreview::class)
    private fun initBtn() {
        with(binding.mainTb) {
            lifecycleScope.launchWhenCreated {
                appBarNoBackBtnFlCart.setClickEventWithDuration(lifecycleScope) {
                    startActivity(Intent(this@MainActivity, CartActivity::class.java))
                }
                appBarNoBackBtnIvProfile.setClickEventWithDuration(lifecycleScope) {
                    startActivity(Intent(this@MainActivity, OrderActivity::class.java))
                }
            }
        }
    }

    private fun initBinding() {
        with(binding) {
            vm = dishViewModel
            lifecycleOwner = this@MainActivity
        }
    }

    private fun initView() {
        setTabTitleArray()
        setTabWithViewPager()
    }

    private fun setTabTitleArray() {
        tabTitleArray = resources.getStringArray(R.array.main_tab_text)
    }

    private fun setTabWithViewPager() {
        binding.mainVp.adapter = TabViewPagerAdapter(supportFragmentManager, lifecycle)
        TabLayoutMediator(binding.mainTl, binding.mainVp) { tab, position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }
}