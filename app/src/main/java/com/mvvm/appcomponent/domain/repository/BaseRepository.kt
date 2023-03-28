package com.mvvm.appcomponent.domain.repository

import com.mvvm.appcomponent.data.remote.AppLevelApi
import com.mvvm.appcomponent.di.AppModule
import com.mvvm.appcomponent.domain.model.response.UserDetailsData
import com.mvvm.appcomponent.util.networkBoundResourceWithoutDb
import retrofit2.http.GET
import javax.inject.Inject

class BaseRepository @Inject constructor(
    @AppModule.OtherAppLevelApi private val otherAppLevelApi: AppLevelApi
) {
    //call app level api here like access token & hash key api

  fun getUserDetails()= networkBoundResourceWithoutDb {
      otherAppLevelApi.getUserDetails()
  }

}