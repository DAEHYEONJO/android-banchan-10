package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

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
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
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

    private fun handleStateChange(state: UiState<List<UiExhibitionItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding.exhibitionPb.toVisible()
                binding.errorLayout.errorCl.toGone()
            }
            is UiState.Success -> {
                Log.e(TAG, "exhibition success")
                binding.exhibitionPb.toGone()
                binding.exhibitionRv.toVisible()
                exhibitionAdapter.submitList(state.items)
            }
            is UiState.ShowToast -> {
                requireContext().showToast(state.message)
            }
            is UiState.Error -> {
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
        if (exhibitionViewModel.exhibitionState.value is UiState.Error) {
            exhibitionViewModel.getExhibitionList()
        }
    }
}