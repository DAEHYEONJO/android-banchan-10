package com.woowahan.android10.deliverbanchan.domain.model

data class UiDetailInfo(
    val hash: String,
    val title: String,
    var isInserted: Boolean,
    val image: String,
    val description: String,
    val point: String,
    val deliveryInfo: String,
    val deliveryFee: String,
    val thumbList: List<String>,
    val detailSection: List<String>,
    val sPrice: Int,
    val nPrice: Int = 0,
    val salePercentage: Int = 0,
    val itemCount: Int = 1
) {
    companion object {
        fun returnEmptyItem() = UiDetailInfo(
            hash = "",
            title = "",
            isInserted = false,
            image = "",
            description = "",
            point = "",
            deliveryInfo = "",
            deliveryFee = "",
            thumbList = emptyList(),
            detailSection = emptyList(),
            sPrice = 0
        )
    }
}