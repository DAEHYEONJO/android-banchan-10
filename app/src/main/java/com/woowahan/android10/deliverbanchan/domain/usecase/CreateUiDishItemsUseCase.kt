package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.data.remote.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreateUiDishItemsUseCase @Inject constructor(
    private val getDishListByThemeUseCase: GetDishListByThemeUseCase,
    private val createEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val mapUiDishItemUseCase: MapUiDishItemUseCase,
    private val mapUiDishItemListUseCase: MapUiDishItemListUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase
) {

    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
        var result: Flow<BaseResult<List<UiDishItem>, Int>> = emptyFlow()

        val supervisorJob = SupervisorJob()
        val scope = CoroutineScope(Dispatchers.IO + supervisorJob)

        scope.launch {
            result = getDishListByThemeUseCase(theme)
                .map { result ->
                    when (result) {
                        is BaseResult.Success -> {

                            val resultUiDishItemList =
                                MutableList<UiDishItem>(result.data.size) { createEmptyUiDishItemUseCase() }

                            result.data.mapIndexed { index, dishItem ->
                                Log.e("CreateUiDishItemsUseCase", "loop")
                                async {
                                    Log.e("CreateUiDishItemsUseCase", "async index : ${index}")
                                    val isInserted = isExistCartInfoUseCase(dishItem.detailHash)
                                    resultUiDishItemList[index] =
                                        mapUiDishItemUseCase(dishItem, isInserted)
                                }
                            }.awaitAll()
                            Log.e("CreateUiDishItemsUseCase", "await all")

                            BaseResult.Success(resultUiDishItemList)
                        }
                        is BaseResult.Error -> {
                            BaseResult.Error(errorCode = result.errorCode)
                        }
                    }
                }
        }.join()

        return result
    }

//    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
//        return getDishListByThemeUseCase(theme)
//            .map { response ->
//                when (response) {
//                    is BaseResult.Success -> {
//                        val resultUiDishItemList = mapUiDishItemListUseCase(response.data)
//                        BaseResult.Success(resultUiDishItemList)
//                    }
//                    is BaseResult.Error -> {
//                        BaseResult.Error(errorCode = response.errorCode)
//                    }
//                }
//            }
//    }
}