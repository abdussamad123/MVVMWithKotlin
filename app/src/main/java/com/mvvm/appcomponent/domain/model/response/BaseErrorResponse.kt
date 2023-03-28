package com.mvvm.appcomponent.domain.model.response

data class BaseErrorResponse(var code: Int,
                             var message: String,
                             var success: Boolean,
                             var data: Any
                             )
