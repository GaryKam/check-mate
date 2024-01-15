package com.oukschub.checkmate.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseUtil {
    private val auth = FirebaseAuth.getInstance()

    fun isLoggedIn(): Boolean = auth.currentUser != null

    fun getUserId(): String = auth.currentUser!!.uid

    fun getDisplayName(): String = auth.currentUser?.displayName ?: ""

    fun signOut() = auth.signOut()
}
