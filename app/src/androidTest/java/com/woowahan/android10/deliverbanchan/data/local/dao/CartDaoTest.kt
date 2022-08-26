package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CartDaoTest {

    private lateinit var database: FoodRoomDatabase
    private lateinit var dao: CartDao
    private lateinit var dummyList: List<CartInfo>

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FoodRoomDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.cartDao()

        dummyList = mutableListOf<CartInfo>().apply {
            repeat(10000){
                add(
                    CartInfo(it.toString(), it%2==0, 20)
                )
            }
        }
    }

    @Test
    fun insertCartInfoList() = runTest {
        val start = System.currentTimeMillis()
        dummyList.forEach { cartInfo ->
            dao.insertCartInfo(cartInfo)
        }
        val end = System.currentTimeMillis()
        println((end-start)/1000)
    }

    @Test
    fun insertVarCartInfoList() = runTest {
        val start = System.currentTimeMillis()
        dao.insertCartInfoVarArg(*dummyList.toTypedArray())
        val end = System.currentTimeMillis()
        println((end-start)/1000)
    }

    @After
    fun teardown() {
        database.close()
    }
}