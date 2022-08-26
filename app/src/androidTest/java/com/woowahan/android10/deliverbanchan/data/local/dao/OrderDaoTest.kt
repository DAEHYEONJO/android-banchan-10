package com.woowahan.android10.deliverbanchan.data.local.dao

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.woowahan.android10.deliverbanchan.data.local.db.FoodRoomDatabase
import com.woowahan.android10.deliverbanchan.data.local.model.entity.OrderInfo
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class OrderDaoTest {

    private lateinit var database: FoodRoomDatabase
    private lateinit var dao: OrderDao
    private lateinit var dummyList: List<OrderInfo>

    @Before // 테스트케이스 시작전 각각 호출
    fun setup() {  // 테스트 용도이므로 inMemory 사용
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            FoodRoomDatabase::class.java
        ).allowMainThreadQueries().build()  // 테스트 코드에서는 db 메인스레드 허용

        dao = database.orderDao()

        dummyList = mutableListOf<OrderInfo>().apply {
            repeat(10000){
                add(
                    OrderInfo(it.toString(), System.currentTimeMillis(), 20, true, 10000)
                )
            }
        }
    }

    // 성능 테스트용도
    @Test
    fun updateForLoopTest() = runTest{
        repeat(10000){
            dao.updateOrderInfo(it.toString(), true)
        }
    }

    @Test
    fun updateTransactionTest() = runTest{
        dao.updateOrderInfoByHashList(dummyList.map { it.hash })
    }

    @After
    fun teardown() {
        database.close()
    }
}