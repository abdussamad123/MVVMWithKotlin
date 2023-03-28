package com.mvvm.appcomponent.domain.model.response

data class BaseSuccessResponse(
    val code: Int,
    val message: String,
    val success: Boolean
)