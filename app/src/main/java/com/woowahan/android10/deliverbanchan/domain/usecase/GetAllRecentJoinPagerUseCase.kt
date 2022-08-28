package com.woowahan.android10.deliverbanchan.domain.usecase

import android.util.Log
import androidx.paging.PagingData
import androidx.paging.map
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class GetAllRecentJoinPagerUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    companion object{
        const val TAG = "GetAllRecentJoinPagerUseCase"
    }
    operator fun invoke(): Flow<PagingData<UiDishItem>> {
        return recentViewedRepository.getAllRecentJoinPager().flow.map { pagingData ->
            pagingData.map { recentViewed ->
                withContext(dispatcher) {
                    val nPrice = recentViewed.nPrice
                    val sPrice = recentViewed.sPrice
                    val percentage =
                        if (nPrice == 0) 0 else 100 - (sPrice.toDouble() / nPrice * 100).toInt()
                    //Log.e(TAG, "i유즈케이스: ${recentViewed.title} ${isInserted}", )
                    UiDishItem(
                        _id = recentViewed._id,
                        hash = recentViewed.hash,
                        title = recentViewed.title,
                        image = recentViewed.image,
                        description = recentViewed.description,
                        salePercentage = percentage,
                        nPrice = recentViewed.nPrice,
                        sPrice = recentViewed.sPrice,
                        timeStamp = recentViewed.timeStamp,
                        isInserted = recentViewed.isInserted
                    )
                }
            }
        }
    }
}