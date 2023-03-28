package com.mvvm.appcomponent.util

import android.util.Log
import androidx.viewbinding.BuildConfig

fun log(tag: String = "", message: String) {
    if (BuildConfig.DEBUG)
        Log.d(tag, "$tag: $message")
}