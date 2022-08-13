package com.woowahan.android10.deliverbanchan.data.remote.dao

import com.woowahan.android10.deliverbanchan.data.remote.model.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Path

interface DishApi {
    @GET("onban/{theme}")
    suspend fun getDishesByTheme(
        @Path("theme") theme: String
    ): Response<DishResponse>

    @GET("onban/best")
    suspend fun getExhibitionDishes(): Response<Exhibition>

    @GET("onban/detail/{hash}")
    suspend fun getDetailDish(
        @Path("hash") hash: String
    ): Response<DishDetail>
}