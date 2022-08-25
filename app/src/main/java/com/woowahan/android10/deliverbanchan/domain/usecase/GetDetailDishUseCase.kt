package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDetailInfo
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

// 정리 완료
@ActivityRetainedScoped
class GetDetailDishUseCase @Inject constructor(
    private val dishDetailRepository: DishDetailRepository
){
    suspend operator fun invoke(hash: String, uiDishItem: UiDishItem, itemCount: Int): Flow<BaseResult<UiDetailInfo, Int>>{
        return dishDetailRepository.getDetailDish(hash).map { result ->
            when(result){
                is BaseResult.Success -> {
                    result.data.thumbImages
                    BaseResult.Success(UiDetailInfo(
                        hash = uiDishItem.hash,
                        title = uiDishItem.title,
                        isInserted = uiDishItem.isInserted,
                        image = uiDishItem.image,
                        description = uiDishItem.description,
                        point = result.data.point,
                        deliveryInfo = result.data.deliveryInfo,
                        deliveryFee = result.data.deliveryFee,
                        thumbList = result.data.thumbImages,
                        detailSection = result.data.detailSection,
                        sPrice = uiDishItem.sPrice,
                        nPrice = uiDishItem.nPrice,
                        salePercentage = uiDishItem.salePercentage,
                        itemCount = itemCount
                    ))
                }
                is BaseResult.Error -> {
                    BaseResult.Error(result.errorCode)
                }
            }

        }
    }
}