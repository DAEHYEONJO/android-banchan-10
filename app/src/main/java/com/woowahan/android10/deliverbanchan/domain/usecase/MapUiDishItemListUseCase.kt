package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.DishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.log

@Singleton
class MapUiDishItemListUseCase @Inject constructor(
    private val mapUiDishItemUseCase: MapUiDishItemUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
) {
    suspend operator fun invoke(dishItemList: List<DishItem>): List<UiDishItem> {
        val uiDishItemList =
            MutableList<UiDishItem>(dishItemList.size) { UiDishItem.returnEmptyItem() }
        coroutineScope { // coroutineScope = 자체가 suspend 함수
            dishItemList.mapIndexed { index, dishItem ->
                async(Dispatchers.IO) {
                    val isInserted = isExistCartInfoUseCase(dishItem.detailHash)
                    uiDishItemList[index] = mapUiDishItemUseCase(dishItem, isInserted)
                }
            }
        }

        return uiDishItemList
    }
}