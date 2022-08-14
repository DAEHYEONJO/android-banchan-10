package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiExhibitionItemsUseCase @Inject constructor(
    private val getExhibitionListUseCase: GetExhibitionListUseCase,
    private val createUiDishItemListUseCase: CreateUiDishItemListUseCase
) {
    suspend operator fun invoke(): Flow<BaseResult<List<UiExhibitionItem>, Int>> {
        return getExhibitionListUseCase()
            .map { response ->
                when (response) {
                    is BaseResult.Success -> {
                        BaseResult.Success(
                            response.data.mapIndexed { index, categoryItem ->
                                UiExhibitionItem(
                                    categoryId = categoryItem.categoryId,
                                    categoryName = categoryItem.name,
                                    uiDishItems = createUiDishItemListUseCase(categoryItem.items)
                                )
                            }
                        )
                    }
                    is BaseResult.Error -> {
                        Log.e("CreateUiExhibitionItemsUseCase", "error")
                        BaseResult.Error(errorCode = response.errorCode)
                    }
                }
            }
    }
}