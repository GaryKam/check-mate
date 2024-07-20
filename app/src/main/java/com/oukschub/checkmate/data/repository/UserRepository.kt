package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.util.FirebaseUtil
import timber.log.Timber
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
        Timber.d("Name: $_displayName")
        Timber.d("User Id: ${FirebaseAuth.getInstance().currentUser!!.uid}")
    }

    fun setNewDisplayName(displayName: String) {
        if (displayName != _displayName) {
            Timber.d("Setting name: $displayName")
            FirebaseUtil.setDisplayName(displayName)
            _displayName = displayName
        }
    }

    fun signOut() {
        Timber.d("Signing out: $_displayName")
        FirebaseUtil.signOut()
        _displayName = ""
    }
}
