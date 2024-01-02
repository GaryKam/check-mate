package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.data.database.Database

class SignInViewModel(
    private val database: Database = Database()
) : ViewModel() {
    fun signIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit,
        onError: () -> Unit
    ) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    database.addUserToDb()
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure()
                }
        } else {
            onError()
        }
    }
}
