package com.woowahan.android10.deliverbanchan.data.remote.model

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
    val nPrice: String = "",
    @SerialName("s_price")
    val sPrice: String,
    @SerialName("title")
    val title: String
)