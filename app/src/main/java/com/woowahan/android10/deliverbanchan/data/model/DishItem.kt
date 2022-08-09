package com.woowahan.android10.deliverbanchan.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DishItem(
    @SerialName("alt")
    val alt: String,
    @SerialName("badge")
    val badge: List<String> = emptyList(),
    @SerialName("delivery_type")
    val deliveryType: List<String>,
    @SerialName("description")
    val description: String,
    @SerialName("detail_hash")
    val detailHash: String,
    @SerialName("image")
    val image: String,
    @SerialName("n_price")
    val nPrice: String = "", // 원래 가격
    @SerialName("s_price")
    val sPrice: String, // 할인된 가격
    @SerialName("title")
    val title: String
)