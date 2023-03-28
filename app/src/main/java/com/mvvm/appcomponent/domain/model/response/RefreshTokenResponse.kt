package com.mvvm.appcomponent.domain.model.response

import java.io.Serializable


data class RefreshTokenResponse(val code:Int, val data: RefreshTokenResponseData,
                                val message:String, val success:Boolean):Serializable

data class RefreshTokenResponseData(val id:Int,
                                    val accessToken:String,
                                    val refreshToken:String,

)
