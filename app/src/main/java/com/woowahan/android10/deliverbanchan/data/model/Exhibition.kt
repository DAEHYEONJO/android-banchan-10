package com.woowahan.android10.deliverbanchan.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exhibition(
    @SerialName("body")
    val body: List<Body>,
    @SerialName("statusCode")
    val statusCode: Int
) {
    @Serializable
    data class Body(
        @SerialName("category_id")
        val categoryId: String,
        @SerialName("items")
        val items: List<DishItem>,
        @SerialName("name")
        val name: String
    )
}