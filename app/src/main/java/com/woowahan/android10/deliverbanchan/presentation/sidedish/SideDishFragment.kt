package com.woowahan.android10.deliverbanchan.presentation.sidedish

import android.os.Bundle
import android.view.View
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentSidedishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SideDishFragment: BaseFragment<FragmentSidedishBinding>(R.layout.fragment_sidedish, "SideDishFragment") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}