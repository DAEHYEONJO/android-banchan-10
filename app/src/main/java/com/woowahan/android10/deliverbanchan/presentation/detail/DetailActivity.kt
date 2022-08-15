package com.woowahan.android10.deliverbanchan.presentation.detail

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.woowahan.android10.deliverbanchan.R
import com.woowahan.android10.deliverbanchan.databinding.ActivityDetailBinding
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.presentation.base.BaseActivity

class DetailActivity : BaseActivity<ActivityDetailBinding>(R.layout.activity_detail, "DetailActivity") {

    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = intent
        val currentUiDishItem = intent.getParcelableExtra<UiDishItem>("UiDishItem")
        currentUiDishItem?.let {
            detailViewModel.currentUiDishItem.value = currentUiDishItem
        }

        initView()
    }

    private fun initView() {

    }
}