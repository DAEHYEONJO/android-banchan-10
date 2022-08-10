package com.woowahan.android10.deliverbanchan.utils.di

import com.woowahan.android10.deliverbanchan.data.remote.DishApi
import com.woowahan.android10.deliverbanchan.data.repository.DishRemoteRepositoryImpl
import com.woowahan.android10.deliverbanchan.domain.repository.DishRemoteRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun providesDishRemoteRepository(dishRemoteRepositoryImpl: DishRemoteRepositoryImpl): DishRemoteRepository

}

