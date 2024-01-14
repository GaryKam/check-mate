package com.oukschub.checkmate.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseUtil {
    private val auth = FirebaseAuth.getInstance()

    fun isLoggedIn() = auth.currentUser != null

    fun getUserId() = auth.currentUser!!.uid

    fun getDisplayName() = auth.currentUser!!.displayName

    fun signOut() = auth.signOut()
}
