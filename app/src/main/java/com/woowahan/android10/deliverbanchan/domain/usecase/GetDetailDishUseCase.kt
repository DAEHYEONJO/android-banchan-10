package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.DishDetail
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetDetailDishUseCase @Inject constructor(
    private val dishDetailRepository: DishDetailRepository
){
    suspend operator fun invoke(hash: String): Flow<BaseResult<DishDetail.DishDetailData, Int>>{
        return dishDetailRepository.getDetailDish(hash)
    }
}