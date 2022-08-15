package com.woowahan.android10.deliverbanchan.presentation.cart.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartMainBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartMainFragment : BaseFragment<FragmentCartMainBinding>(
    R.layout.fragment_cart_main, "CartMainFragment"
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}