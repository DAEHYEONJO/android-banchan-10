package com.woowahan.android10.deliverbanchan.data.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DetailDish(
    @SerialName("data")
    val data: Data,
    @SerialName("hash")
    val hash: String
) {
    @Serializable
    data class Data(
        @SerialName("delivery_fee")
        val deliveryFee: String,
        @SerialName("delivery_info")
        val deliveryInfo: String,
        @SerialName("detail_section")
        val detailSection: List<String>,
        @SerialName("point")
        val point: String,
        @SerialName("prices")
        val prices: List<String>,
        @SerialName("product_description")
        val productDescription: String,
        @SerialName("thumb_images")
        val thumbImages: List<String>,
        @SerialName("top_image")
        val topImage: String
    )
}