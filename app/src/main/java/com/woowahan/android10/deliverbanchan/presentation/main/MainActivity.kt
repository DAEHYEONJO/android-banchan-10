package com.woowahan.android10.deliverbanchan.presentation.main

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.room.ColumnInfo
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.common.TAB_EXHIBITION
import com.woowahan.android10.deliverbanchan.common.TAB_MAIN_DISH
import com.woowahan.android10.deliverbanchan.common.TAB_SIDE_DISH
import com.woowahan.android10.deliverbanchan.common.TAB_SOUP_DISH
import com.woowahan.android10.deliverbanchan.data.local.model.*
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.viewmodel.DishViewModel
import com.woowahan.android10.deliverbanchan.utils.TabViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {

    private val dishViewModel: DishViewModel by viewModels()
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