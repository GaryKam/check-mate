package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.util.FirebaseUtil

class ProfileViewModel : ViewModel() {
    fun getDisplayName(): String {
        return FirebaseUtil.getDisplayName()
    }

    fun signOut() {
        FirebaseUtil.signOut()
    }
}
