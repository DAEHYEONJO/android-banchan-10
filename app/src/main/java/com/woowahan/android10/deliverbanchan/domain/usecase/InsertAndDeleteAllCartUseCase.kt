package com.woowahan.android10.deliverbanchan.domain.usecase

import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class InsertAndDeleteAllCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
){
    suspend operator fun invoke(cartInfo: List<CartInfo>, deleteHashes: List<String>){
        cartRepository.insertAndDeleteAllItems(cartInfo, deleteHashes)
    }
}