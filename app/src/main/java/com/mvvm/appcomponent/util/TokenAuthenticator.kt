package com.mvvm.appcomponent.util

import android.content.Context
import android.content.Intent
import com.mvvm.MainActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticator @Inject constructor(
        @ApplicationContext context: Context
): Authenticator {

        private val appContext = context.applicationContext
        private val userPreferences = UserPreferences(appContext)
        override fun authenticate(route: Route?, response: Response): Request? {

            userPreferences.coroutineScope.launch {
                userPreferences.clear()
            }
            //change MainActivity with Login or Welcome Activity in below line
            appContext.startActivity(Intent(appContext,MainActivity::class.java).apply {
                flags=Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            })

            return null
        }

}