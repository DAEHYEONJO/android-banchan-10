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
    private val getMainDishListUseCase: GetMainDishListUseCase,
    private val crateEmptyUiDishItemUseCase: CreateEmptyUiDishItemUseCase,
    private val createUiDishItemUseCase: CreateUiDishItemUseCase,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase
) {

    val supervisorJob = SupervisorJob()
    val scope = CoroutineScope(Dispatchers.IO + supervisorJob)

    suspend operator fun invoke(): Flow<BaseResult<List<UiDishItem>, Int>> {
        lateinit var result: Flow<BaseResult<List<UiDishItem>, Int>>
        scope.launch {
            result = getMainDishListUseCase()
                .map { result ->
                    when (result) {
                        is BaseResult.Success -> {
                            Log.e("CreateUiDishItemsUseCase", "success")

                            val resultUiDishItemList =
                                MutableList<UiDishItem>(result.data.size) { crateEmptyUiDishItemUseCase() }

                            result.data.mapIndexed { index, dishItem ->
                                Log.e("CreateUiDishItemsUseCase", "loop")
                                CoroutineScope(Dispatchers.IO).async {
                                    Log.e("CreateUiDishItemsUseCase", "in async")
                                    resultUiDishItemList[index] = createUiDishItemUseCase(dishItem)
                                }
                                //resultUiDishItemList[index] = createUiDishItemUseCase(dishItem)
                            }.awaitAll()

                            Log.e("CreateUiDishItemsUseCase", "await all")
                            BaseResult.Success(resultUiDishItemList)
                        }
                        is BaseResult.Error -> {
                            // Error 처리
                            Log.e("CreateUiDishItemsUseCase", "error")
                            BaseResult.Error(errorCode = result.errorCode)
                        }
                    }
                }
        }.join() // join 없으면 에러

        return result
    }
}