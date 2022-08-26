package com.woowahan.android10.deliverbanchan.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exhibition(
    @SerialName("body")
    val body: List<CategoryItem>,
    @SerialName("statusCode")
    val statusCode: Int
) {
    @Serializable
    data class CategoryItem(
        @SerialName("category_id")
        val categoryId: String,
        @SerialName("items")
        val items: List<DishItem>,
        @SerialName("name")
        val name: String
    )
}