package com.woowahan.android10.deliverbanchan.data.remote.repository

import com.woowahan.android10.deliverbanchan.data.remote.dao.DishApi
import com.woowahan.android10.deliverbanchan.data.remote.mapper.ApiMapper
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.response.BaseResult
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.model.UiExhibitionItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DishItemRepositoryImpl @Inject constructor(
    private val cartRepository: CartRepository,
    private val dishApi: DishApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : DishItemRepository {

    companion object {
        const val TAG = "DishItemRepositoryImpl"
    }

    override suspend fun getDishesByTheme(theme: String): Flow<BaseResult<List<UiDishItem>>> =
        flow {
            kotlin.runCatching {
                dishApi.getDishesByTheme(theme)
            }.onFailure { throwable ->
                emit(BaseResult.Error(throwable.message.toString()))
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val apiDishResponse = response.body()!!.body
                    coroutineScope {
                        emit(
                            BaseResult.Success(
                                apiDishResponse.mapIndexed { index, dishItem ->
                                    async(dispatcher) {
                                        val isInserted =
                                            cartRepository.isExistCartInfo(dishItem.detailHash)
                                        ApiMapper.mapToUiDishItem(dishItem, isInserted, index)
                                    }
                                }.awaitAll().toList()
                            )
                        )
                    }
                } else {
                    emit(BaseResult.Error(response.code().toString()))
                }
            }
        }

    override suspend fun getExhibitionDishes(): Flow<BaseResult<List<UiExhibitionItem>>> =
        flow {
            kotlin.runCatching {
                dishApi.getExhibitionDishes()
            }.onFailure { throwable ->
                emit(BaseResult.Error(throwable.message.toString()))
            }.onSuccess { response ->
                if (response.isSuccessful) {
                    val apiCategoryResponse = response.body()!!.body
                    coroutineScope {
                        emit(
                            BaseResult.Success(
                                apiCategoryResponse.mapIndexed { index, categoryItem ->
                                    val categoryId = categoryItem.categoryId
                                    val categoryName = categoryItem.name
                                    val uiDishItemList =
                                        categoryItem.items.mapIndexed { index, dishItem ->
                                            async {
                                                val isInserted = cartRepository.isExistCartInfo(dishItem.detailHash)
                                                ApiMapper.mapToUiDishItem(dishItem, isInserted, 0)
                                            }
                                        }.awaitAll().toList()
                                    UiExhibitionItem(
                                        categoryId,
                                        categoryName,
                                        uiDishItemList
                                    )
                                }.toList()
                            )
                        )
                    }
                } else {
                    emit(BaseResult.Error(response.code().toString()))
                }
            }
        }
}