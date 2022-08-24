package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentExhibitionBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
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

    override fun onResume() {
        super.onResume()
        checkErrorState()
    }

    private fun initView() {
        setExhibitionHeaderAdpater()
        setExhibitionContentAdapter()
        setRecyclerView()
        setErrorBtn()
        initObserver()
    }

    private fun setExhibitionHeaderAdpater() {
        exhibitionHeaderAdapter = ExhibitionHeaderAdapter()
        exhibitionHeaderAdapter.submitList(listOf("header").toList())
    }

    private fun setExhibitionContentAdapter() {
        exhibitionAdapter = ExhibitionAdapter().apply {
            dishItemClickListener = this@ExhibitionFragment
        }
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
            is ExhibitionUiState.IsLoading -> {
                binding.exhibitionPb.toVisible()
                binding.errorLayout.errorCl.toGone()
            }
            is ExhibitionUiState.Success -> {
                binding.exhibitionPb.toGone()
                binding.exhibitionRv.toVisible()
                exhibitionAdapter.submitList(state.uiExhibitionItems)
            }
            is ExhibitionUiState.ShowToast -> {
                requireContext().showToast(state.message)
            }
            is ExhibitionUiState.Error -> {
                binding.exhibitionPb.toGone()
                binding.exhibitionRv.toGone()
                binding.errorLayout.errorCl.toVisible()
            }
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setOnClickListener {
            exhibitionViewModel.getExhibitionList()
        }
    }

    private fun checkErrorState() {
        if (exhibitionViewModel.exhibitionState.value is ExhibitionUiState.Error) {
            exhibitionViewModel.getExhibitionList()
        }
    }
}