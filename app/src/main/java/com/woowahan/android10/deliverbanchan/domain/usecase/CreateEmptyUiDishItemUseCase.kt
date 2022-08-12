package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateEmptyUiDishItemUseCase @Inject constructor() {
    suspend operator fun invoke() = UiDishItem(
        "", "", false, "", "", 0, 0, "0%", 0
    )
}