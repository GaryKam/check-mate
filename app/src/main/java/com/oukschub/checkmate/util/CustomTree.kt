package com.oukschub.checkmate.util

import timber.log.Timber

class CustomTree(
    private val tagPrefix: String
) : Timber.DebugTree() {
    override fun log(
        priority: Int,
        tag: String?,
        message: String,
        t: Throwable?
    ) {
        super.log(priority, tagPrefix + tag, message, t)
    }
}
