package com.woowahan.android10.deliverbanchan.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.dao.DishDao
import com.woowahan.android10.deliverbanchan.data.local.dao.OrderDao
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentlyViewedDao
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.LocalDish
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.entity.RecentlyViewedInfo

@Database(
    entities = [CartInfo::class, RecentlyViewedInfo::class, LocalDish::class, OrderInfo::class],
    version = 1,
    exportSchema = false
)
abstract class FoodRoomDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
    abstract fun dishDao(): DishDao
}