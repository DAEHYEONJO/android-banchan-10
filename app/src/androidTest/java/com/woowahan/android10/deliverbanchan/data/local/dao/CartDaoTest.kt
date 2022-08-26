package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.model.entity.CartInfo
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
            repeat(10000){
                add(
                    CartInfo(it.toString(), it%2==0, 20)
                )
            }
        }
    }

    @Test
    fun insertCartInfoList() = runTest {
        dummyList.forEach { cartInfo ->
            dao.insertCartInfo(cartInfo)
        }
    }

    @Test
    fun insertVarCartInfoList() = runTest {
        dao.insertCartInfoVarArg(*dummyList.toTypedArray())
    }

    @After // 테스트케이스 완료시 각각 호출
    fun teardown() {
        database.close()
    }
}