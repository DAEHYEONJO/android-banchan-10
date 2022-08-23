package com.woowahan.android10.deliverbanchan.presentation.main.host

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.common.ORDER_REQUEST_CODE
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.main.sidedish.SideDishViewModel
import com.woowahan.android10.deliverbanchan.presentation.main.soupdish.SoupViewModel
import com.woowahan.android10.deliverbanchan.presentation.order.OrderActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main, "MainActivity") {

    private val dishViewModel: DishViewModel by viewModels()
    private lateinit var tabTitleArray: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadOrderRequestCode()
        initBinding()
        initView()
        initBtn()
        dishViewModel.cartInfoState.onEach {
            Log.e("DishViewModel", "onCreate: $it")
        }.launchIn(lifecycleScope)
    }

    private fun loadOrderRequestCode() {
        val sharedPreferences = getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        if(sharedPreferences.contains(KEY_ORDER_REQUEST_CODE)){
            val orderRequestCode = sharedPreferences.getInt(KEY_ORDER_REQUEST_CODE, 1)
            ORDER_REQUEST_CODE = orderRequestCode
        }
        Log.e(TAG, "load shared : ${ORDER_REQUEST_CODE}")
    }

    private fun saveOrderRequestCode() {
        val sharedPreferences = getSharedPreferences(KEY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_ORDER_REQUEST_CODE, ORDER_REQUEST_CODE)
        editor.apply()
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

    override fun onStop() {
        saveOrderRequestCode()
        super.onStop()
    }

    companion object{
        private const val KEY_SHARED_PREFERENCES = "key_shared_preferences"
        private const val KEY_ORDER_REQUEST_CODE = "key_order_request_code"
    }
}