package com.woowahan.android10.deliverbanchan.presentation.maindish

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentMaindishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.maindish.adapter.MainDishLinearAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainDishFragment: BaseFragment<FragmentMaindishBinding>(R.layout.fragment_maindish, "MainDishFragment") {

    private val mainDishViewModel: MainDishViewModel by viewModels()
    private lateinit var mainDishLinearAdapter: MainDishLinearAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setRadioGroupListener()
        setRecyclerView()
        collectData()
        getData()
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

    private fun setRecyclerView() {
        mainDishLinearAdapter = MainDishLinearAdapter {
            // bottom sheet 에 uiDishItem 전달하기
            Log.e("TAG", "cart icon clicked")
            val cartBottomSheetFragment = CartBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelable("UiDishItem", it)
            cartBottomSheetFragment.arguments = bundle
            cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
        }
        binding.maindishRv.apply {
            adapter = mainDishLinearAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun collectData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainDishViewModel.mainDishListFlow.collect {
                    mainDishLinearAdapter.submitList(it.toList())
                }
            }
        }
    }

    private fun getData() {
        mainDishViewModel.getMainDishList()
    }
}