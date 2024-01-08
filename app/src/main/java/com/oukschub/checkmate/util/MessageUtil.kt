package com.oukschub.checkmate.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

object MessageUtil {
    fun displayToast(
        context: Context,
        @StringRes resId: Int,
    ) {
        Toast.makeText(
            context,
            resId,
            Toast.LENGTH_SHORT
        ).show()
    }
}
