package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.util.MessageUtil

class SignInViewModel : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set

    fun signIn(onSuccess: () -> Unit) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { onSuccess() }
                .addOnFailureListener { MessageUtil.displayToast(R.string.sign_in_failure) }
        } else {
            if (email.isBlank()) {
                emailError = MessageUtil.getStringFromRes(R.string.sign_in_email_error)
            }

            if (password.isBlank()) {
                passwordError = MessageUtil.getStringFromRes(R.string.sign_in_password_error)
            }
        }
    }

    fun updateEmail(email: String) {
        this.email = email
        emailError = ""
    }

    fun updatePassword(password: String) {
        this.password = password
        passwordError = ""
    }
}
