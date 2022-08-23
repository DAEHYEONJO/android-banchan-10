package com.woowahan.android10.deliverbanchan.presentation.base

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment

abstract class BaseActivity<T: ViewDataBinding>(
    @LayoutRes private val layoutResId: Int,
    val TAG: String
): AppCompatActivity(), CartDialogFragment.TextClickListener  {
    private var _binding: T? = null
    val binding get() = checkNotNull(_binding){
        "$TAG Activity Binding Null"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate: ")
        _binding = DataBindingUtil.setContentView(this, layoutResId)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: ")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: ")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: ")
    }

    override fun moveToCartTextClicked() {
        startActivity(Intent(this, CartActivity::class.java))
    }
}