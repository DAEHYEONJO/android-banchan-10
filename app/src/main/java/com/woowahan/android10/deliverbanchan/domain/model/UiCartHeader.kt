package com.woowahan.android10.deliverbanchan.domain.model

data class UiCartHeader(
    var checkBoxText: String,
    var checkBoxChecked: Boolean
) {
    companion object {
        const val TEXT_SELECT_ALL = "전체 선택"
        const val TEXT_SELECT_RELEASE = "선택 해제"
        fun emptyItem() = UiCartHeader(
            "", false
        )
    }
}