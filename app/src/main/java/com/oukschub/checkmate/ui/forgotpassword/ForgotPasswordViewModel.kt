package com.oukschub.checkmate.ui.forgotpassword

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var emailError by mutableIntStateOf(R.string.blank)
        private set

    fun resetPassword(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        if (email.isNotBlank()) {
            Firebase.auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Timber.d("Sent password reset email to $email")
                        onSuccess()
                    } else {
                        Timber.d("Failed to send password reset email to $email")
                        onFailure()
                    }
                }
        } else {
            emailError = R.string.forgot_password_email_error
        }
    }

    fun changeEmail(email: String) {
        this.email = email
        emailError = R.string.blank
    }
}
