package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.badge.BadgeDrawable
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.main.sidedish.SideDishViewModel
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
    }

    private fun initBinding() {
        with(binding){
            vm = dishViewModel
            lifecycleOwner = this@MainActivity
        }
    }

    private fun initView() {
        setTabTitleArray()
        setTabWithViewPager()
        initObserver()
    }

    private fun initObserver() {
        dishViewModel.cartInfoState.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .onEach { state ->
                Log.e(TAG, "initObserver: $state", )
            }.launchIn(lifecycleScope)
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