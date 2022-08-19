package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class DeleteCartInfoByHashUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    companion object {
        const val TAG = "DeleteCartInfoByHashUseCase"
    }
    suspend operator fun invoke(hash: String){
        Log.e(TAG, "invoke: delete $hash", )
        cartRepository.deleteCartInfo(hash)
    }
}