package com.mvvm.appcomponent.util

import android.content.Context
import android.content.Intent
import com.google.gson.Gson
import com.mvvm.MainActivity
import com.mvvm.appcomponent.domain.model.response.RefreshTokenResponse
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.Response
import org.json.JSONObject
import javax.inject.Inject

const val CODE_ACCESS_TOKEN_EXPIRED = 401
const val CODE_ACCESS_TOKEN_BLACKLISTED = 402

class CustomInterceptor @Inject constructor(
    private val isPostLogin: Boolean = true,
    private val userPreferences: UserPreferences,
    private val isAuthenticationRequired: Boolean,
    private val context: Context? = null
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        lateinit var response: Response
        val request = chain.request()
        response =
            if (isAuthenticationRequired)
                chain.proceed(addHeaderInRequest(request.newBuilder()).build())
            else chain.proceed(request.newBuilder().build())

        if (response.code == CODE_ACCESS_TOKEN_EXPIRED) {
            synchronized(this) {
                log("CustomInterceptor", "intercept: Auth token expired, refreshing")
                response.close()
                val requestBody = JSONObject().accumulate(
                    "refreshToken",
                    userPreferences.getStringDataFromDataStore(AppPrefKey.REFRESH_TOKEN)
                )
                val requestBuilder = Request.Builder()
                requestBuilder.addHeader("Accept", "application/json")
                requestBuilder.addHeader("Authorization", com.mvvm.BuildConfig.PRE_LOGIN_ACCESS_TOKEN)
                requestBuilder.url(AppPrefKey.RENEW_ACCESS_TOKEN_URL)
                requestBuilder.post(
                    RequestBody.create(
                        "application/json".toMediaTypeOrNull(),
                        requestBody.toString()
                    )
                )
                val refreshTokenResponse = chain.proceed(requestBuilder.build())

                if (refreshTokenResponse.code == 200) {

                    log("CustomInterceptor", "intercept: Successfully refreshed Auth token")

                    val refreshTokenResponse = Gson().fromJson( refreshTokenResponse.body?.string(),
                        RefreshTokenResponse::class.java )

                    runBlocking {
                        refreshTokenResponse?.data?.let {
                            userPreferences.saveStringDataToDataStore(
                                AppPrefKey.ACCESS_TOKEN,
                                it.accessToken
                            )
                            userPreferences.saveStringDataToDataStore(
                                AppPrefKey.REFRESH_TOKEN,
                                it.refreshToken
                            )

                            response = chain.proceed(addHeaderInRequest(request.newBuilder()).build())
                        }
                    }
                } else {
                    log(
                        "CustomInterceptor",
                        "intercept: logging out due failure to refresh token, HTTP Status code: ${refreshTokenResponse.code}"
                    )
                    logout()
                }
            }
        } else if (response.code == CODE_ACCESS_TOKEN_BLACKLISTED) {
            log("CustomInterceptor", "intercept: logging out due to HTTP Status code: 402")
            logout()
        }
        return response
    }

    private fun logout() {
        runBlocking {
           //remove isLogin pref key here from data store

            context?.let {
                it.startActivity(
                    Intent(it,
                        MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                )
            }

        }

    }

    private fun addHeaderInRequest(reqBuilder: Request.Builder): Request.Builder {
        reqBuilder.addHeader("Accept", "application/json")
        .also {
            if (isPostLogin)
                reqBuilder.addHeader(
                    "Authorization", userPreferences.getStringDataFromDataStore(
                        AppPrefKey.ACCESS_TOKEN
                    )
                )
            else
                reqBuilder.addHeader("Authorization", com.mvvm.BuildConfig.PRE_LOGIN_ACCESS_TOKEN)
        }
        return reqBuilder
    }
}