package com.woowahan.android10.deliverbanchan.domain.model.response

sealed class BaseResult<out T>{
    data class Success<T> (val data: T): BaseResult<T>()
    data class Error (val error: String): BaseResult<Nothing>()
}
