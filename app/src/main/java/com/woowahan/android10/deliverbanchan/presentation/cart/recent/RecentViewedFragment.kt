package com.woowahan.android10.deliverbanchan.presentation.cart.recent

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.FragmentRecentViewedBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseFragment
import com.woowahan.android10.deliverbanchan.presentation.cart.CartViewModel
import com.woowahan.android10.deliverbanchan.presentation.cart.recent.adapter.RecentPagingAdapter
import com.woowahan.android10.deliverbanchan.presentation.common.decorator.GridSpanCountTwoDecorator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RecentViewedFragment : BaseFragment<FragmentRecentViewedBinding>(
    R.layout.fragment_recent_viewed,
    "RecentlyViewedFragment"
) {
    @Inject
    lateinit var recentPagingDataAdapter: RecentPagingAdapter
    private val cartViewModel: CartViewModel by activityViewModels()
    private val recentViewModel: RecentViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initObserver()
    }

    private fun initObserver() {
        recentViewModel.recentJoinItem.observe(viewLifecycleOwner) {
            lifecycleScope.launch {
                recentPagingDataAdapter.submitData(it)
            }
        }

    }

    private fun initRecyclerView() {
        with(binding.recentlyViewedProductRv) {
            adapter = recentPagingDataAdapter.apply {
                onDishItemClickListener = this@RecentViewedFragment
                lifecycleScope.launch {
                    loadStateFlow.collectLatest { loadState ->
                        binding.recentlyViewedProductTvEmpty.isVisible =
                            loadState.append.endOfPaginationReached && recentPagingDataAdapter.itemCount == 0
                        if (loadState.append.endOfPaginationReached && recentPagingDataAdapter.itemCount == 0) {
                            Log.e(TAG, "텅 빈 상 태")
                        }
                        if (loadState.source.refresh is LoadState.Loading) {
                            Log.e(TAG, " 불러오는 상태")
                        }
                    }
                }
            }
            if (itemDecorationCount == 0) {
                addItemDecoration(GridSpanCountTwoDecorator(requireContext()))
            }
        }
    }

}