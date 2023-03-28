package com.mvvm.appcomponent.util

import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class TokenAuthenticatorPreLogin @Inject constructor(): Authenticator {
    override fun authenticate(route: Route?, response: Response): Request?{
        return  null
    }
}