package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentRecentViewedBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter.RecentPagingAdapter
import com.woowahan.android10.deliverbanchan.presentation.cart.viewmodel.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoDecorator
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.ext.toVisible
import com.woowahan.android10.deliverbanchan.presentation.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@AndroidEntryPoint
class RecentViewedFragment : BaseFragment<FragmentRecentViewedBinding>(
    R.layout.fragment_recent_viewed,
    "RecentlyViewedFragment"
) {
    @Inject
    lateinit var recentPagingDataAdapter: RecentPagingAdapter

    @Inject
    lateinit var gridSpanCountTwoDecorator: GridSpanCountTwoDecorator
    private val recentViewModel: RecentViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()
    }

    private fun initObserver() {
        cartViewModel.allCartJoinState.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            if (it is UiState.Success){
                Log.e(TAG, "initObserver: 리센트 -> 카트 변경됨", )
                recentViewModel.getRecentJoinPagingFlow()
                recentPagingDataAdapter.notifyDataSetChanged()
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        recentViewModel.recentJoinItem.flowWithLifecycle(viewLifecycleOwner.lifecycle).onEach {
            recentPagingDataAdapter.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun initRecyclerView() {
        with(binding.recentlyViewedProductRv) {
            adapter = recentPagingDataAdapter.apply {
                if (itemDecorationCount == 0) addItemDecoration(gridSpanCountTwoDecorator)
                onDishItemClickListener = this@RecentViewedFragment
                lifecycleScope.launchWhenResumed {
                    loadStateFlow.collect { loadState ->
                        binding.recentlyViewedProductTvEmpty.isVisible =
                            loadState.append.endOfPaginationReached && recentPagingDataAdapter.itemCount == 0

                        if (loadState.source.refresh is LoadState.Loading) {
                            binding.recentPb.toVisible()
                        }

                        if (loadState.source.refresh is LoadState.NotLoading) {
                            binding.recentPb.toGone()
                            if (itemDecorationCount == 0) addItemDecoration(
                                gridSpanCountTwoDecorator
                            )
                        }
                    }
                }
            }

        }
    }


}