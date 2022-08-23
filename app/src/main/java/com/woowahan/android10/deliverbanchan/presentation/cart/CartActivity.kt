package com.woowahan.android10.deliverbanchan.presentation.cart

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.fragment.app.commit
import androidx.work.ExistingWorkPolicy
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.background.CartItemsDbWorker
import com.woowahan.android10.deliverbanchan.background.getOneTimeRequestBuilder
import com.woowahan.android10.deliverbanchan.databinding.ActivityCartBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.cart.complete.CartDeliveryCompleteFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.main.CartMainFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.RecentViewedFragment
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart, "CartActivity"){
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

    private fun makeWorkRequest(){
        Log.e(TAG, "makeWorkRequest: 워크매니저콜", )
        val worker = WorkManager.getInstance(application)
        val workRequest = getOneTimeRequestBuilder<CartItemsDbWorker>(cartViewModel.getCartWorkerData())
        worker.enqueue(workRequest)
    }

    override fun onStop() {
        super.onStop()
        cartViewModel.updateAllCartItemChanged()
        //makeWorkRequest()
    }
}
