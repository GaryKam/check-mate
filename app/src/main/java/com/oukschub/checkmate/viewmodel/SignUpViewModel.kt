package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.util.MessageUtil

class SignUpViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val passwordRegex = Regex("^(?=\\S+\$).{8,20}\$")

    fun signUp(onSuccess: () -> Unit) {
        val validEmail = emailRegex.matches(email)
        val validPassword = passwordRegex.matches(password)

        if (validEmail && validPassword) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    database.addUserToDb()
                    onSuccess()
                }
                .addOnFailureListener {
                    MessageUtil.displayToast(R.string.sign_up_failure)
                }
        } else {
            if (!validEmail) {
                emailError = "Email is invalid"
            }

            if (!validPassword) {
                passwordError = "Password is invalid"
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
