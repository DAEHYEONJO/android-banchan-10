package com.woowahan.android10.deliverbanchan.data.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishSoup(
    @SerialName("body")
    val body: List<DishItem>,
    @SerialName("statusCode")
    val statusCode: Int
)