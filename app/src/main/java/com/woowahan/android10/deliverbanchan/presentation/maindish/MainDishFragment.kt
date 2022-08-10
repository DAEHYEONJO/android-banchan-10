package com.woowahan.android10.deliverbanchan.presentation.maindish

import android.os.Bundle
import android.view.View
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentMaindishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDishFragment: BaseFragment<FragmentMaindishBinding>(R.layout.fragment_maindish, "MainDishFragment") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
    }
}