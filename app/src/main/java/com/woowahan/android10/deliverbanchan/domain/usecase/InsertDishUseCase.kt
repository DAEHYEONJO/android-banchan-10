package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InsertLocalDishUseCase @Inject constructor(
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(localDish: LocalDish){
        dishRepository.insertLocalDish(localDish)
    }
}