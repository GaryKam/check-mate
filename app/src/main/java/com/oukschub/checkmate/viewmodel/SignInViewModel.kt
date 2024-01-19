package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var emailError by mutableIntStateOf(R.string.blank)
        private set
    var passwordError by mutableIntStateOf(R.string.blank)
        private set

    fun signIn(
        onSuccess: () -> Unit,
        onFailure: (Int) -> Unit
    ) {
        if (email.isNotBlank() && password.isNotBlank()) {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    viewModelScope.launch {
                        repository.loadChecklists()
                        onSuccess()
                    }
                }
                .addOnFailureListener { onFailure(R.string.sign_in_failure) }
        } else {
            if (email.isBlank()) {
                emailError = R.string.sign_in_email_error
            }

            if (password.isBlank()) {
                passwordError = R.string.sign_in_password_error
            }
        }
    }

    fun changeEmail(email: String) {
        this.email = email
        emailError = R.string.blank
    }

    fun changePassword(password: String) {
        this.password = password
        passwordError = R.string.blank
    }
}
