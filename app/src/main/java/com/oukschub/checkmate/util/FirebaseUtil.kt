package com.oukschub.checkmate.util

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

/**
 * Utilities to interact with Firebase.
 */
object FirebaseUtil {
    private val auth = FirebaseAuth.getInstance()

    fun isSignedIn(): Boolean = auth.currentUser != null

    fun getUserId(): String = auth.currentUser!!.uid

    fun getDisplayName(): String = auth.currentUser?.displayName ?: ""

    fun setDisplayName(displayName: String) {
        FirebaseAuth.getInstance().currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
        )
    }

    fun signOut() = auth.signOut()
}
