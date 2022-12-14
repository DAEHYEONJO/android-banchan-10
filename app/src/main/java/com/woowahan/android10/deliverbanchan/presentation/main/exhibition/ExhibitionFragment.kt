package com.woowahan.android10.deliverbanchan.presentation.main.exhibition

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentExhibitionBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.main.exhibition.adapter.ExhibitionAdapter
import com.woowahan.android10.deliverbanchan.presentation.main.exhibition.adapter.ExhibitionHeaderAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        setExhibitionHeaderAdapter()
        setExhibitionContentAdapter()
        setRecyclerView()
        setErrorBtn()
        initObserver()
    }

    private fun setExhibitionHeaderAdapter() {
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
        exhibitionViewModel.exhibitionState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            handleStateChange(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun handleStateChange(state: UiState<List<UiExhibitionItem>>) {
        when (state) {
            is UiState.Loading -> {
                binding.exhibitionPb.toVisible()
            }
            is UiState.Success -> {
                binding.exhibitionPb.toGone()
                binding.errorLayout.errorCl.toGone()
                binding.exhibitionRv.toVisible()
                exhibitionAdapter.submitList(state.items)
            }
            is UiState.Error -> {
                binding.exhibitionPb.toGone()
                binding.exhibitionRv.toGone()
                binding.errorLayout.errorCl.toVisible()
            }
        }
    }

    private fun setErrorBtn() {
        binding.errorLayout.errorBtn.setClickEventWithDuration(coroutineScope = viewLifecycleOwner.lifecycleScope) {
            exhibitionViewModel.getExhibitionList()
        }
    }

    private fun checkErrorState() {
        if (exhibitionViewModel.exhibitionState.value is UiState.Error) {
            exhibitionViewModel.getExhibitionList()
        }
    }
}