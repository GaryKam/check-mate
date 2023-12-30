package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel : ViewModel() {
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