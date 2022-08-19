package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.main.sidedish.SideDishViewModel
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupViewModel
import com.woowahan.android10.deliverbanchan.presentation.order.OrderActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {

    private val dishViewModel: DishViewModel by viewModels()
    private val soupViewModel: SoupViewModel by viewModels()
    private val sideDishViewModel: SideDishViewModel by viewModels()
    private lateinit var tabTitleArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        initView()
        initBtn()
    }

    private fun initBtn() {
        with(binding.mainTb) {
            appBarNoBackBtnFlCart.setOnClickListener {
                startActivity(Intent(this@MainActivity, CartActivity::class.java))
            }
            appBarNoBackBtnIvProfile.setOnClickListener {
                startActivity(Intent(this@MainActivity, OrderActivity::class.java))
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