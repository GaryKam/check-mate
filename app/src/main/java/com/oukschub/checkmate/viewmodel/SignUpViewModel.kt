package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.util.MessageUtil

class PasswordCheck(password: String) {
    val lengthMinCheck = password.length >= 8
    val lengthMaxCheck = password.length < 30
    val lowercaseCheck = password.contains(Regex("[a-z]"))
    val uppercaseCheck = password.contains(Regex("[A-Z]"))
    val digitCheck = password.contains(Regex("\\d"))
    val characterCheck = password.contains(Regex("[!@#\$%^&*()`~?,<.>]"))
    val isValid =
        lengthMinCheck &&
            lengthMaxCheck &&
            lowercaseCheck &&
            uppercaseCheck &&
            digitCheck &&
            characterCheck
}

class SignUpViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordMatch by mutableStateOf("")
        private set
    var emailError by mutableStateOf("")
        private set
    var passwordError by mutableStateOf("")
        private set
    var passwordMatchError by mutableStateOf("")
        private set
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")

    fun signUp(onSuccess: () -> Unit) {
        val validEmail = emailRegex.matches(email)
        val passwordCheck = PasswordCheck(password)
        val matchPasswordCheck = passwordMatch == password

        if (validEmail && passwordCheck.isValid && matchPasswordCheck) {
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
                emailError = MessageUtil.getStringFromRes(R.string.sign_up_email_invalid)
            }

            var passwordErrorMessage = ""

            if (!passwordCheck.lengthMinCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_min_length)
            }

            if (!passwordCheck.lengthMaxCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_max_length)
            }

            if (!passwordCheck.lowercaseCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_lowercase)
            }

            if (!passwordCheck.uppercaseCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_uppercase)
            }

            if (!passwordCheck.digitCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_digit)
            }

            if (!passwordCheck.characterCheck) {
                passwordErrorMessage += MessageUtil.getStringFromRes(R.string.sign_up_pw_character)
            }

            if (!passwordCheck.isValid) {
                passwordError = passwordErrorMessage
            }

            if (!matchPasswordCheck) {
                passwordMatchError = MessageUtil.getStringFromRes(R.string.sign_up_pw_match_error)
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

    fun updateMatchPassword(passwordMatch: String) {
        this.passwordMatch = passwordMatch
        passwordMatchError = ""
    }
}
