package com.woowahan.android10.deliverbanchan.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityDetailBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.common.showToast
import com.woowahan.android10.deliverbanchan.presentation.common.toGone
import com.woowahan.android10.deliverbanchan.presentation.common.toVisible
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailContentAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailSectionImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailThumbImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.state.DetailUiState
import com.woowahan.android10.deliverbanchan.presentation.state.ExhibitionUiState
import kotlinx.coroutines.launch

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail, "DetailActivity") {

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var detailThumbImageAdapter: DetailThumbImageAdapter
    private lateinit var detailContentAdapter: DetailContentAdapter
    private lateinit var detailSectionImageAdapter: DetailSectionImageAdapter
    private lateinit var concatAdapter: ConcatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val currentUiDishItem = intent.getParcelableExtra<UiDishItem>("UiDishItem")
        currentUiDishItem?.let {
            detailViewModel.currentUiDishItem.value = currentUiDishItem
        }

        initView()
        observeApiState()
    }

    private fun initView() {
        detailViewModel.getDetailDishInfo()
        setReyclerView()
    }

    private fun setReyclerView() {
        detailThumbImageAdapter = DetailThumbImageAdapter()
        detailContentAdapter = DetailContentAdapter()
        detailSectionImageAdapter = DetailSectionImageAdapter()

        concatAdapter = ConcatAdapter(detailThumbImageAdapter, detailContentAdapter, detailSectionImageAdapter)

        binding.detailRv.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)
        }

    }

    private fun observeApiState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.detailState.collect {
                    handleStateChange(it)
                }
            }
        }
    }

    private fun handleStateChange(state: DetailUiState) {
        when (state) {
            is DetailUiState.IsLoading -> {
                //binding.maindishPb.toVisible()
                }
            is DetailUiState.Success -> {
                //binding.maindishPb.toGone()
            }
            is DetailUiState.ShowToast -> {
                //binding.maindishPb.toGone()
                showToast(state.message)
            }
        }
    }
}