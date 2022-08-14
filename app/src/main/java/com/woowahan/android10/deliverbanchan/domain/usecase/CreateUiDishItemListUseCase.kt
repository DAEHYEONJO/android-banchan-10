package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDishItemListUseCase @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val createUiDishItemUseCase: CreateUiDishItemUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
) {
    suspend operator fun invoke(dishItemList: List<DishItem>): List<UiDishItem> {
        val uiDishItemList =
            MutableList<UiDishItem>(dishItemList.size) { createEmptyUiDishItemUseCase() }

        coroutineScope {
            dishItemList.mapIndexed { index, dishItem ->
                async(Dispatchers.IO) {
                    uiDishItemList[index] = createUiDishItemUseCase(dishItem)
                }
            }.awaitAll()
        }

        return uiDishItemList
    }
}