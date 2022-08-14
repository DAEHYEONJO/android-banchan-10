package com.woowahan.android10.deliverbanchan.presentation.main.maindish

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentMaindishBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.main.common.MainGridAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainDishFragment :
    BaseFragment<FragmentMaindishBinding>(R.layout.fragment_maindish, "MainDishFragment") {

    private val mainDishViewModel: MainDishViewModel by viewModels()
    private lateinit var mainDishLinearAdapter: MainDishLinearAdapter
    //private lateinit var mainDishGridAdapter: MainDishGridAdapter
    @Inject lateinit var mainDishAdapter: MainGridAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        Log.e(TAG, "onViewCreated: ${binding.maindishTvHeader.paintFlags}")
    }

    private fun initView() {
        setRadioGroupListener()
        setRecyclerView()
        initObserver()
        getData()
    }

    private fun setRadioGroupListener() {
        binding.maindishRg.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.maindish_rb_grid -> {
                    binding.maindishRv.apply {
                        adapter = mainDishAdapter
                        layoutManager = GridLayoutManager(requireContext(), 2)
                    }
                    mainDishAdapter.submitList(mainDishViewModel.mainDishList.toList())
                }
                R.id.maindish_rb_linear -> {
                    binding.maindishRv.apply {
                        adapter = mainDishLinearAdapter
                        layoutManager = LinearLayoutManager(requireContext())
                    }
                    mainDishLinearAdapter.submitList(mainDishViewModel.mainDishList.toList())
                }
            }
        }
    }

    private fun setRecyclerView() {

        mainDishAdapter.apply {
            cartIconClick = {
                val cartBottomSheetFragment = CartBottomSheetFragment()

                cartBottomSheetFragment.setDialogDismissWhenInsertSuccessListener(object: CartBottomSheetFragment.DialogDismissWhenInsertSuccessListener{
                    override fun dialogDismissWhenInsertSuccess(hash: String, title: String) {
                        mainDishViewModel.changeMainDishItemIsInserted(hash)
                        val cartDialog = CartDialogFragment()

                        cartDialog.setTextClickListener(object : CartDialogFragment.TextClickListener {
                            override fun moveToCartTextClicked(hash: String, title: String) {
                                Log.e("MainDishFragment", "move to cart, hash : ${hash}, title : ${title}")

                                // CartActivity 이동 하면서 title, hash 전달 예정
                            }
                        })

                        val bundle = Bundle()
                        bundle.putString("hash", hash)
                        bundle.putString("title", title)
                        cartDialog.arguments = bundle
                        cartDialog.show(childFragmentManager, "CartDialog")
                        Log.e("TAG", "현재 선택된 상품명 : ${title}")
                    }
                })

                val bundle = Bundle()
                bundle.putParcelable("UiDishItem", it)
                cartBottomSheetFragment.arguments = bundle
                cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
            }
        }

        mainDishLinearAdapter = MainDishLinearAdapter {
            Log.e("TAG", "cart icon clicked")
            val cartBottomSheetFragment = CartBottomSheetFragment()
            val bundle = Bundle()
            bundle.putParcelable("UiDishItem", it)
            cartBottomSheetFragment.arguments = bundle
            cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
        }

        binding.maindishRv.apply {
            adapter = mainDishAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                mainDishViewModel.mainDishState.collect {
                    handleStateChange(it)
                }
            }
        }
    }

    private fun handleStateChange(state: UiState) {
        when (state) {
            is UiState.IsLoading -> binding.maindishPb.toVisible()
            is UiState.Success -> {
                binding.maindishPb.toGone()
                mainDishAdapter.submitList(state.uiDishItems)
            }
            is UiState.ShowToast -> {
                binding.maindishPb.toGone()
                requireContext().showToast(state.message)
            }
        }
    }

    private fun getData() {
        mainDishViewModel.getMainDishList()
    }
}