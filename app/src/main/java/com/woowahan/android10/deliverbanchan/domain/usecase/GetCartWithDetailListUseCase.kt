package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.di.MainImmediateDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetCartWithDetailListUseCase @Inject constructor(
    private val getAllCartInfoUseCase: GetAllCartInfoUseCase,
    private val getDetailDishUseCase: GetDetailDishUseCase,
    @MainImmediateDispatcher private val dispatcher: CoroutineDispatcher
){
}