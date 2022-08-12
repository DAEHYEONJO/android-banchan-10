package com.woowahan.android10.deliverbanchan.data.remote.dao

import com.woowahan.android10.deliverbanchan.data.remote.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface DishApi {
    @GET("onban/side")
    suspend fun getSideDishes(): Response<DishSide>

    @GET("onban/soup")
    suspend fun getSoupDishes(): Response<DishSoup>

    @GET("onban/main")
    suspend fun getMainDishes(): Response<DishMain>

    @GET("onban/best")
    suspend fun getExhibitionDishes(): Response<Exhibition>

    @GET("onban/detail/{hash}")
    suspend fun getDetailDish(
        @Path("hash") hash: String
    ): Response<DishDetail>
}