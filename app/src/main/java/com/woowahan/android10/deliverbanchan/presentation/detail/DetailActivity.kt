package com.woowahan.android10.deliverbanchan.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityDetailBinding
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity
import com.woowahan.android10.deliverbanchan.presentation.base.click_listener.OnCartDialogClickListener
import com.woowahan.android10.deliverbanchan.presentation.cart.CartActivity
import com.woowahan.android10.deliverbanchan.presentation.common.ext.setClickEventWithDuration
import com.woowahan.android10.deliverbanchan.presentation.common.ext.showToast
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailContentAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailSectionImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.detail.adapter.DetailThumbImageAdapter
import com.woowahan.android10.deliverbanchan.presentation.dialogs.dialog.CartDialogFragment
import com.woowahan.android10.deliverbanchan.presentation.order.OrderActivity
import com.woowahan.android10.deliverbanchan.presentation.state.UiDetailState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
        binding.vm = detailViewModel
        binding.lifecycleOwner = this
        initBtn()
        initView()
        initObservers()
    }

    @OptIn(FlowPreview::class)
    private fun initBtn() {
        with(binding.detailTb) {
            lifecycleScope.launchWhenCreated {
                appBarNoBackBtnFlCart.setClickEventWithDuration(lifecycleScope) {
                    startActivity(Intent(this@DetailActivity, CartActivity::class.java))
                }
                appBarNoBackBtnIvProfile.setClickEventWithDuration(lifecycleScope) {
                    startActivity(Intent(this@DetailActivity, OrderActivity::class.java))
                }
            }
        }
    }

    private fun initView() {
        setRecyclerView()
    }

    private fun setRecyclerView() {
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

    private fun initObservers() {

        detailViewModel.uiDetailInfo.flowWithLifecycle(lifecycle).onEach { uiDetailState ->
            when (uiDetailState) {
                is UiDetailState.Loading -> {

                }
                is UiDetailState.Success -> {
                    detailThumbImageAdapter.submitList(listOf(uiDetailState.uiDishItems.thumbList))
                    detailContentAdapter.submitList(listOf(uiDetailState.uiDishItems))
                    detailSectionImageAdapter.submitList(uiDetailState.uiDishItems.detailSection)
                }
                is UiDetailState.ShowToast -> {
                    showToast(uiDetailState.message)
                }
                is UiDetailState.Error -> {
                    showToast(uiDetailState.errorCode.toString() + "에러 에러 삐용 삐용")
                }
            }
        }.launchIn(lifecycleScope)

        detailViewModel.insertSuccessEvent.flowWithLifecycle(lifecycle).onEach {
            Log.e(TAG, "인서트이벤트 $it: ")
            if (it) {
                cartDialog.show(supportFragmentManager, "CartDialog")
            } else {
                showToast("장바구니 담기에 실패했습니다.")
            }
        }.launchIn(lifecycleScope)

    }



}