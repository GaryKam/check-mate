package com.oukschub.checkmate.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseUtil {
    fun isLoggedIn() = FirebaseAuth.getInstance().currentUser != null

    fun getUserId() = FirebaseAuth.getInstance().currentUser!!.uid

    fun signOut() = FirebaseAuth.getInstance().signOut()
}