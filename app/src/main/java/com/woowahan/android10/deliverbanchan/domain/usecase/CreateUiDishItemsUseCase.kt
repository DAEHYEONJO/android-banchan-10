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
    private val mapUiDishItemListUseCase: MapUiDishItemListUseCase,
) {

//    val supervisorJob = SupervisorJob()
//    val scope = CoroutineScope(Dispatchers.IO + supervisorJob)

//    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
//        var result: Flow<BaseResult<List<UiDishItem>, Int>> = emptyFlow()
//
//        val supervisorJob = SupervisorJob()
//        val scope = CoroutineScope(Dispatchers.IO + supervisorJob)
//
//        scope.launch {
//            Log.e("CreateUiDishItemsUseCase", "1 ${Thread.currentThread().name}")
//            result = getDishListByThemeUseCase(theme)
//                .map { result ->
//                    when (result) {
//                        is BaseResult.Success -> {
//                            Log.e("CreateUiDishItemsUseCase", "success")
//
//                            val resultUiDishItemList =
//                                MutableList<UiDishItem>(result.data.size) { UiDishItem.returnEmptyItem() }
//
//                            result.data.mapIndexed { index, dishItem ->
//                                Log.e("CreateUiDishItemsUseCase", "loop")
////                                coroutineScope { // 각각 coroutineScope 블록 순차적으로 불림
////                                    async(Dispatchers.IO) {
////                                        Log.e("CreateUiDishItemsUseCase", "in async")
////                                        resultUiDishItemList[index] = createUiDishItemUseCase(dishItem)
////                                    }
////                                }
//                                CoroutineScope(Dispatchers.IO).async {
//                                    Log.e("CreateUiDishItemsUseCase", "async index : ${index}")
//                                    Log.e(
//                                        "CreateUiDishItemsUseCase",
//                                        "2 ${Thread.currentThread().name}"
//                                    )
//                                    val isInserted = isExistCartInfoUseCase(dishItem.detailHash)
//                                    resultUiDishItemList[index] =
//                                        mapUiDishItemUseCase(dishItem, isInserted)
//                                }
//                            }.awaitAll()
//
//                            Log.e("CreateUiDishItemsUseCase", "await all")
//                            BaseResult.Success(resultUiDishItemList)
//                        }
//                        is BaseResult.Error -> {
//                            // Error 처리
//                            Log.e("CreateUiDishItemsUseCase", "error")
//                            BaseResult.Error(errorCode = result.errorCode)
//                        }
//                    }
//                }
//        }.join() // join 없으면 에러
//
//        return result
//    }

    suspend operator fun invoke(theme: String): Flow<BaseResult<List<UiDishItem>, Int>> {
        return getDishListByThemeUseCase(theme)
            .map { response ->
                when (response) {
                    is BaseResult.Success -> {
                        val resultUiDishItemList = mapUiDishItemListUseCase(response.data)
                        BaseResult.Success(resultUiDishItemList)
                    }
                    is BaseResult.Error -> {
                        BaseResult.Error(errorCode = response.errorCode)
                    }
                }
            }
    }
}