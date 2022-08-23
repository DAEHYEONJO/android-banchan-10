package com.woowahan.android10.deliverbanchan.presentation.base.click_listener

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem

interface OnDishItemClickListener {
    fun onClickCartIcon(uiDishItem: UiDishItem)
    fun onClickDish(uiDishItem: UiDishItem)
}