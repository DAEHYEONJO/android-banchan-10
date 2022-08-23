package com.woowahan.android10.deliverbanchan.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.base.click_listener.OnCartDialogClickListener
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailContentAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailSectionImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailThumbImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.state.DetailUiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity :
    BaseActivity<ActivityDetailBinding>(R.layout.activity_detail, "DetailActivity"),
    OnCartDialogClickListener {

    private val detailViewModel: DetailViewModel by viewModels()

    private lateinit var detailThumbImageAdapter: DetailThumbImageAdapter
    private lateinit var detailContentAdapter: DetailContentAdapter
    private lateinit var detailSectionImageAdapter: DetailSectionImageAdapter
    private lateinit var concatAdapter: ConcatAdapter
    private val cartDialog: CartDialogFragment by lazy {
        CartDialogFragment()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        observeApiState()
        observeDetailData()
        observeInsertSuccess()
    }

    private fun initView() {
        setReyclerView()
    }

    private fun setReyclerView() {
        detailThumbImageAdapter = DetailThumbImageAdapter()
        detailContentAdapter = DetailContentAdapter({
            // minus click
            detailViewModel.minusItemCount()
        }, {
            // plus click
            detailViewModel.plusItemCount()
        }, {
            // button click
            detailViewModel.orderDetailItem()
        })
        detailSectionImageAdapter = DetailSectionImageAdapter()

        concatAdapter =
            ConcatAdapter(detailThumbImageAdapter, detailContentAdapter, detailSectionImageAdapter)

        binding.detailRv.apply {
            adapter = concatAdapter
            layoutManager = LinearLayoutManager(this@DetailActivity)
        }

        val animator = binding.detailRv.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
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

    private fun observeDetailData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.thumbList.collect {
                    detailThumbImageAdapter.submitList(listOf(it))
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.uiDetailInfo.collect {
                    detailContentAdapter.submitList(listOf(it))
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.sectionList.collect {
                    detailSectionImageAdapter.submitList(it.toList())
                }
            }
        }
    }

    private fun observeInsertSuccess() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailViewModel.insertSuccessEvent.collect {
                    Log.e(TAG, "인서트이벤트 $it: ")
                    if (it) {
                        cartDialog.show(supportFragmentManager, "CartDialog")
                    } else {
                        showToast("장바구니 담기에 실패했습니다.")
                    }
                }
            }
        }
    }



}