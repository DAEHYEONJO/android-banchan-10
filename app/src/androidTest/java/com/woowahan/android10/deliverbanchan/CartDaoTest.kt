package com.woowahan.android10.deliverbanchan

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.woowahan.android10.deliverbanchan.data.local.dao.CartDao
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
import kotlinx.coroutines.Dispatchers
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

    @Before // 테스트케이스 시작전 각각 호출
    fun setup() {  // 테스트 용도이므로 inMemory 사용
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FoodRoomDatabase::class.java
        ).allowMainThreadQueries().build()  // 테스트 코드에서는 db 메인스레드 허용

        dao = database.cartDao()

        dummyList = mutableListOf<CartInfo>().apply {
            repeat(100) {
                add(
                    CartInfo(it.toString(), it % 2 == 0, 20)
                )
            }
        }
    }

    @After // 테스트케이스 완료시 각각 호출
    fun teardown() {
        database.close()
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
}