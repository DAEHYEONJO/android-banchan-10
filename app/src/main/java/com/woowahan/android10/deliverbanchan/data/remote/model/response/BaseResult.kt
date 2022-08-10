package com.woowahan.android10.deliverbanchan.data.remote.model.response

sealed class BaseResult<out T, out U>{
    data class Success<T> (val data: T): BaseResult<T, Nothing>()
    data class Error<U> (val errorCode: U): BaseResult<Nothing, U>()
}
