package com.mvvm.appcomponent.data.remote

import com.mvvm.appcomponent.domain.model.response.UserDetailsData
import retrofit2.http.GET

interface AppLevelApi {

  //call app level api here like access token & hash key api

    @GET("users")
    suspend fun getUserDetails(): List<UserDetailsData>

}