package com.woowahan.android10.deliverbanchan.di

import com.woowahan.android10.deliverbanchan.data.local.repository.CartRepositoryImpl
import com.woowahan.android10.deliverbanchan.data.local.repository.OrderRepositoryImpl
import com.woowahan.android10.deliverbanchan.data.local.repository.RecentlyViewedRepositoryImpl
import com.woowahan.android10.deliverbanchan.data.remote.repository.DishDetailRepositoryImpl
import com.woowahan.android10.deliverbanchan.data.remote.repository.DishItemRepositoryImpl
import com.woowahan.android10.deliverbanchan.domain.repository.local.CartRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.OrderRepository
import com.woowahan.android10.deliverbanchan.domain.repository.local.RecentlyViewedRepository
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishDetailRepository
import com.woowahan.android10.deliverbanchan.domain.repository.remote.DishItemRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindsDishDetailRepository(dishDetailRepositoryImpl: DishDetailRepositoryImpl): DishDetailRepository

    @Binds
    @Singleton
    abstract fun bindsDishItemRepository(dishItemRepositoryImpl: DishItemRepositoryImpl): DishItemRepository

    @Binds
    @Singleton
    abstract fun bindsRecentlyViewedRepository(recentlyViewedRepositoryImpl: RecentlyViewedRepositoryImpl): RecentlyViewedRepository

    @Binds
    @Singleton
    abstract fun bindsOrderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    @Singleton
    abstract fun bindsCartRepository(cartRepositoryImpl: CartRepositoryImpl): CartRepository

}

