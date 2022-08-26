package com.woowahan.android10.deliverbanchan.domain.repository.local

import androidx.annotation.WorkerThread
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.join.Cart
import com.woowahan.android10.deliverbanchan.domain.model.UiBottomSheet
import com.woowahan.android10.deliverbanchan.domain.model.UiCartOrderDishJoinItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    @WorkerThread
    fun getAllCartInfo(): Flow<List<CartInfo>>

    @WorkerThread
    fun getBottomSheetCartInfoByHash(hash: String): Flow<UiBottomSheet>

    @WorkerThread
    suspend fun insertCartInfo(hash: String, checked: Boolean, amount: Int)

    @WorkerThread
    suspend fun deleteCartInfo(hash: String)

    @WorkerThread
    suspend fun isExistCartInfo(hash: String): Boolean

    @WorkerThread
    fun getAllCartJoinList(): Flow<List<Cart>>

    @WorkerThread
    suspend fun updateCartChecked(hash: String, checked: Boolean)

    @WorkerThread
    suspend fun updateCartAmount(hash: String, amount: Int)

    @WorkerThread
    suspend fun insertCartInfoVarArg(vararg cartInfo: CartInfo)

    @WorkerThread
    suspend fun insertAndDeleteCartItems(
        uiCartOrderDishJoinList: List<UiCartOrderDishJoinItem>,
        deleteHashes: List<String>
    )

    @WorkerThread
    suspend fun deleteCartInfoByHashList(deleteHashes: List<String>)
}