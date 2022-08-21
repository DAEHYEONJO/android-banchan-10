package com.woowahan.android10.deliverbanchan.domain.usecase

import androidx.paging.PagingData
import androidx.paging.map
import com.woowahan.android10.deliverbanchan.di.IoDispatcher
import com.woowahan.android10.deliverbanchan.domain.model.UiDishItem
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentViewedRepository
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class GetAllRecentJoinPagerUseCase @Inject constructor(
    private val recentViewedRepository: RecentViewedRepository,
    private val isExistCartInfoUseCase: IsExistCartInfoUseCase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<PagingData<UiDishItem>> {
        return recentViewedRepository.getAllRecentJoinPager().flow.map { pagingData ->
            pagingData.map { recentViewed ->
                withContext(dispatcher){
                    val isInserted = isExistCartInfoUseCase(recentViewed.hash)
                    UiDishItem(
                        _id = recentViewed._id,
                        hash = recentViewed.hash,
                        title = recentViewed.title,
                        image = recentViewed.image,
                        description = recentViewed.description,
                        nPrice = recentViewed.nPrice,
                        sPrice = recentViewed.sPrice,
                        timeStamp = recentViewed.timeStamp,
                        isInserted = isInserted
                    )
                }
            }
        }
    }
}