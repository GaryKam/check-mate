package com.oukschub.checkmate.util

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import java.lang.ref.WeakReference

object MessageUtil {
    private lateinit var contextRef: WeakReference<Context>

    fun init(context: Context) {
        contextRef = WeakReference(context)
    }

    fun displayToast(
        @StringRes resId: Int
    ) {
        Toast.makeText(
            contextRef.get(),
            resId,
            Toast.LENGTH_SHORT
        ).show()
    }
}
