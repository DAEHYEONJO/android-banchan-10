package com.woowahan.android10.deliverbanchan.presentation.soupdish

import android.os.Bundle
import android.view.View
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSoupdishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SoupDishFragment: BaseFragment<FragmentSoupdishBinding>(R.layout.fragment_soupdish, "SoupDishFragment") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}