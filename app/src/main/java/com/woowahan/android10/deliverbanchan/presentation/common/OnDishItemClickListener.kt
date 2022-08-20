package com.woowahan.android10.deliverbanchan.presentation.common

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

interface OnDishItemClickListener {
    fun onClickCartIcon(uiDishItem: UiDishItem)
    fun onClickDish(uiDishItem: UiDishItem)
}