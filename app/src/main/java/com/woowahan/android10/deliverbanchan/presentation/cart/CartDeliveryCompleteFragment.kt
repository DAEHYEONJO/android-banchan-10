package com.woowahan.android10.deliverbanchan.presentation.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentCartDeliveryCompleteBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CartDeliveryCompleteFragment : BaseFragment<FragmentCartDeliveryCompleteBinding>(
    R.layout.fragment_cart_delivery_complete,
    "CartDeliveryCompleteFragment"
) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}