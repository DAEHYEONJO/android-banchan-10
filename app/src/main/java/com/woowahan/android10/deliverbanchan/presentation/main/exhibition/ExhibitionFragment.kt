package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentExhibitionBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.detail.DetailActivity
import com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExhibitionFragment :
    BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition, "ExhibitionFragment") {

    private val exhibitionViewModel: ExhibitionViewModel by viewModels()
    private lateinit var concatAdapter: ConcatAdapter
    private lateinit var exhibitionHeaderAdapter: ExhibitionHeaderAdapter
    private lateinit var exhibitionAdapter: ExhibitionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setExhibitionHeaderAdpater()
        setExhibitionContentAdapter()
        setRecyclerView()
        initObserver()
    }

    private fun setExhibitionHeaderAdpater() {
        exhibitionHeaderAdapter = ExhibitionHeaderAdapter()
        exhibitionHeaderAdapter.submitList(listOf("header").toList())
    }

    private fun setExhibitionContentAdapter() {
        exhibitionAdapter = ExhibitionAdapter ({
            val cartBottomSheetFragment = CartBottomSheetFragment()

            cartBottomSheetFragment.setDialogDismissWhenInsertSuccessListener(object :
                CartBottomSheetFragment.DialogDismissWhenInsertSuccessListener {
                override fun dialogDismissWhenInsertSuccess(hash: String, title: String) {
                    exhibitionViewModel.changeMainDishItemIsInserted(hash)
                    val cartDialog = CartDialogFragment()

                    cartDialog.setTextClickListener(object : CartDialogFragment.TextClickListener {
                        override fun moveToCartTextClicked(hash: String, title: String) {
                            Log.e("ExhibitionFragment", "move to cart, hash : ${hash}, title : ${title}")

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
        }, {
            // cart icon 영역을 제외한 다른 곳 누를 시
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("UiDishItem", it)
            startActivity(intent)
        })
    }

    private fun setRecyclerView() {
        concatAdapter =
            ConcatAdapter(exhibitionHeaderAdapter, exhibitionAdapter)

        binding.exhibitionRv.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exhibitionViewModel.exhibitionState.collect {
                    Log.e("ExhibitionFragment", "collect")
                    handleStateChange(it)
                }
            }
        }
    }

    private fun handleStateChange(state: ExhibitionUiState) {
        when (state) {
            is ExhibitionUiState.IsLoading -> binding.maindishPb.toVisible()
            is ExhibitionUiState.Success -> {
                binding.maindishPb.toGone()
                exhibitionAdapter.submitList(state.uiExhibitionItems)
            }
            is ExhibitionUiState.ShowToast -> {
                binding.maindishPb.toGone()
                requireContext().showToast(state.message)
            }
        }
    }
}