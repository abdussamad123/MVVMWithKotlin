package com.mvvm.appcomponent.util

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AlertDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mvvm.R

object AppUtils {

    fun getProgressDialog(context: Context?): AlertDialog? {
        if (context == null) return null
        return MaterialAlertDialogBuilder(context)
            .setView(
                R.layout.layout_progress_bar)
            .setBackground(
                ColorDrawable(
                    Color.TRANSPARENT)
            )
            .setCancelable(false)
            .create()
    }
}