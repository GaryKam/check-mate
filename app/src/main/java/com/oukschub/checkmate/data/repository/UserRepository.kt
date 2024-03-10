package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.util.FirebaseUtil
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val database: Database
) {
    private var _displayName by mutableStateOf("")
    val displayName = snapshotFlow { _displayName }

    suspend fun createUser(displayName: String): Boolean {
        val isSuccessful = database.createUser()

        if (isSuccessful) {
            FirebaseUtil.setDisplayName(displayName)
            _displayName = displayName
        }

        return isSuccessful
    }

    fun fetchDisplayName() {
        _displayName = FirebaseUtil.getDisplayName()
    }

    fun setNewDisplayName(displayName: String) {
        FirebaseUtil.setDisplayName(displayName)
        _displayName = displayName
    }

    fun signOut() {
        FirebaseUtil.signOut()
        _displayName = ""
    }
}
