package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {

    private val dishViewModel: DishViewModel by viewModels()
    private val soupViewModel: SoupViewModel by viewModels()
    private lateinit var tabTitleArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
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