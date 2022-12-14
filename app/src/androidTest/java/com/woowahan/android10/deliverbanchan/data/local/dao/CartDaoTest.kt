package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.first
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

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertCartInfoList() = runTest {
        dummyList.forEach { cartInfo ->
            dao.insertCartInfo(cartInfo)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun insertVarCartInfoList() = runTest {
        dao.insertCartInfoVarArg(*dummyList.toTypedArray())
    }

    @Test
    fun insertCartItem() = runTest {
        val cartItem = CartInfo(hash = "HBDEF", checked = true, amount = 5)
        dao.insertCartInfo(cartItem)
        val allCartItems = dao.getAllCartInfo().first()
        Truth.assertThat(allCartItems).contains(cartItem)
    }

    @Test
    fun isInsertedCheck() = runTest {
        dummyList.forEach {
            dao.isExistCartInfo("HBDEF")
        }
    }

    @Test
    fun isInsertedCheckAsync() = runTest {
        dummyList.forEach {
            async(Dispatchers.IO) {
                dao.isExistCartInfo("HBDEF")
            }
        }
    }

    @After
    fun teardown() {
        database.close()
    }
}