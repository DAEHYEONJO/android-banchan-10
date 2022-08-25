package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.domain.repository.local.DishRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject
import javax.inject.Singleton

// 정리 완료
@ActivityRetainedScoped
class InsertLocalDishUseCase @Inject constructor(
    private val dishRepository: DishRepository
) {
    suspend operator fun invoke(localDish: LocalDish){
        dishRepository.insertLocalDish(localDish)
    }
}