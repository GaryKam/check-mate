package com.oukschub.checkmate.util

import timber.log.Timber

/**
 * Adds custom prefix to log statements for easy debugging.
 */
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
