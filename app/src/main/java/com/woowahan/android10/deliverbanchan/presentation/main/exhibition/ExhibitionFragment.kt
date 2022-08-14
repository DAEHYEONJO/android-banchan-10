package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentExhibitionBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.dialogs.bottomsheet.CartBottomSheetFragment
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExhibitionFragment: BaseFragment<FragmentExhibitionBinding>(R.layout.fragment_exhibition, "ExhibitionFragment") {

    private val exhibitionViewModel: ExhibitionViewModel by viewModels()
    private lateinit var exhibitionAdapter: ExhibitionAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        setRecyclerView()
        initObserver()
    }

    private fun setRecyclerView() {
        exhibitionAdapter = ExhibitionAdapter{
            val cartBottomSheetFragment = CartBottomSheetFragment()

            cartBottomSheetFragment.setDialogDismissWhenInsertSuccessListener(object: CartBottomSheetFragment.DialogDismissWhenInsertSuccessListener{
                override fun dialogDismissWhenInsertSuccess(hash: String, title: String) {
                    val cartDialog = CartDialogFragment()
                    cartDialog.show(childFragmentManager, "CartDialog")
                    Log.e("TAG", "현재 선택된 상품명 : ${title}")
                }
            })

            val bundle = Bundle()
            bundle.putParcelable("UiDishItem", it)
            cartBottomSheetFragment.arguments = bundle
            cartBottomSheetFragment.show(childFragmentManager, "CartBottomSheet")
        }
        binding.exhibitionRv.apply {
            adapter = exhibitionAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun initObserver() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                exhibitionViewModel.exhibitionState.collect {
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