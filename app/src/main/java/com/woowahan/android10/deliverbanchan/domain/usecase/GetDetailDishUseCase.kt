package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ActivityRetainedScoped
class GetDetailDishUseCase @Inject constructor(
    private val dishDetailRepository: DishDetailRepository
){
    suspend operator fun invoke(hash: String, uiDishItem: UiDishItem): Flow<BaseResult<UiDetailInfo>>{
        return dishDetailRepository.getDetailDish(hash, uiDishItem)
    }
}