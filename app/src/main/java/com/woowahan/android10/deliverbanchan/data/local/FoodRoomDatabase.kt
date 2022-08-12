package com.woowahan.android10.deliverbanchan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.dao.OrderDao
import com.woowahan.android10.deliverbanchan.data.local.dao.RecentlyViewedDao
import com.woowahan.android10.deliverbanchan.data.local.model.CartInfo
import com.woowahan.android10.deliverbanchan.data.local.model.OrderDish
import com.woowahan.android10.deliverbanchan.data.local.model.OrderInfo
import com.woowahan.android10.deliverbanchan.data.local.model.RecentlyViewedInfo

@Database(
    entities = [CartInfo::class, RecentlyViewedInfo::class, OrderDish::class, OrderInfo::class],
    version = 1,
    exportSchema = false
)
abstract class FoodRoomDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
    abstract fun recentlyViewedDao(): RecentlyViewedDao
}