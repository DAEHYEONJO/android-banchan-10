package com.woowahan.android10.deliverbanchan.di

import android.content.Context
import androidx.room.Room
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.dao.DishDao
import com.woowahan.android10.deliverbanchan.data.local.dao.OrderDao
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentlyViewedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun providesFoodRoomDatabase(
        @ApplicationContext context: Context
    ): FoodRoomDatabase = Room.databaseBuilder(context, FoodRoomDatabase::class.java, "ban_chan.db").build()

    @Singleton
    @Provides
    fun providesCartDao(foodRoomDatabase: FoodRoomDatabase): CartDao = foodRoomDatabase.cartDao()

    @Singleton
    @Provides
    fun providesOrderDao(foodRoomDatabase: FoodRoomDatabase): OrderDao = foodRoomDatabase.orderDao()

    @Singleton
    @Provides
    fun providesRecentlyViewedDao(foodRoomDatabase: FoodRoomDatabase): RecentlyViewedDao = foodRoomDatabase.recentlyViewedDao()

    @Singleton
    @Provides
    fun providesDishDao(foodRoomDatabase: FoodRoomDatabase): DishDao = foodRoomDatabase.dishDao()

}