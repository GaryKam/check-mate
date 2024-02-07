package com.oukschub.checkmate.ui.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.util.FirebaseUtil

class ProfileViewModel : ViewModel() {
    var displayName by mutableStateOf(FirebaseUtil.getDisplayName())
        private set

    fun setNewDisplayName(displayName: String) {
        FirebaseUtil.setDisplayName(displayName)
        this.displayName = displayName
    }

    fun signOut() {
        FirebaseUtil.signOut()
    }
}
