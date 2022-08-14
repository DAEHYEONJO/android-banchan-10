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
class MapUiDishItemListUseCase @Inject constructor(
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val mapUiDishItemUseCase: MapUiDishItemUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
) {
    suspend operator fun invoke(dishItemList: List<DishItem>): List<UiDishItem> {
        val uiDishItemList =
            MutableList<UiDishItem>(dishItemList.size) { createEmptyUiDishItemUseCase() }

        coroutineScope { // coroutineScope = 자체가 suspend 함수
            dishItemList.mapIndexed { index, dishItem ->
                async(Dispatchers.IO) {
                    // 장바구니에 있는지 체크 후 받은 isInserted(Boolean) 도 createUiDishItemUseCase에 넘겨줄 예정
                    uiDishItemList[index] = mapUiDishItemUseCase(dishItem)
                }
            }.awaitAll()
        }

        return uiDishItemList
    }
}