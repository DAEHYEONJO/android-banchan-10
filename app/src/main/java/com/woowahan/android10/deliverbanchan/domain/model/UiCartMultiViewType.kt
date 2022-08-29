package com.woowahan.android10.deliverbanchan.domain.model

import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartBottomBody
import com.woowahan.android10.deliverbanchan.presentation.cart.model.UiCartHeader

data class UiCartMultiViewType(
    val viewType: Int,
    val uiCartHeader: UiCartHeader? = null,
    var uiCartOrderDishJoinItem: UiCartOrderDishJoinItem? = null,
    val uiCartBottomBody: UiCartBottomBody? = null
) {
    companion object{
        const val HEADER = 0
        const val BODY = 1
        const val FOOTER = 2
    }
}
