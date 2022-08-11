package com.woowahan.android10.deliverbanchan.presentation.maindish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentMaindishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainDishFragment: BaseFragment<FragmentMaindishBinding>(R.layout.fragment_maindish, "MainDishFragment") {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setRadioGroupListener()
    }

    private fun setRadioGroupListener() {
        binding.maindishRg.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.maindish_rb_grid -> {
                    Log.e("TAG", "grid radio selected")
                }
                R.id.maindish_rb_linear -> {
                    Log.e("TAG", "linear radio selected")
                }
            }
        }
    }
}