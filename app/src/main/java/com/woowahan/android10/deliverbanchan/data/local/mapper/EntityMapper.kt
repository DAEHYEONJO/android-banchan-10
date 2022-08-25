package com.woowahan.android10.deliverbanchan.data.local.mapper

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiBottomSheet

object EntityMapper {
    fun mapToUiBottomSheet(cartInfo: CartInfo): UiBottomSheet = with(cartInfo){
        UiBottomSheet(
            checked, amount
        )
    }
}