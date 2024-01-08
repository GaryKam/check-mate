package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.database.Database

class SignUpViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordMatch by mutableStateOf("")
        private set
    var emailError by mutableIntStateOf(R.string.blank)
        private set
    private val _passwordChecks = mutableStateListOf<Pair<Boolean, Int>>()
    val passwordChecks: ImmutableList<Pair<Boolean, Int>>
        get() = ImmutableList.copyOf(_passwordChecks)
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val passwordChecker = PasswordChecker()

    init {
        _passwordChecks.addAll(passwordChecker.getChecks())
    }

    fun signUp(
        onSuccess: () -> Unit,
        onFailure: (Int) -> Unit
    ) {
        val validEmail = emailRegex.matches(email)

        if (validEmail && passwordChecker.isValidated) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    database.addUserToDb()
                    onSuccess()
                }
                .addOnFailureListener {
                    onFailure(R.string.sign_up_failure)
                }
        } else {
            if (!validEmail) {
                emailError = R.string.sign_up_email_error
            }
        }
    }

    fun changeEmail(email: String) {
        this.email = email.trim()
        emailError = R.string.blank
    }

    fun changePassword(password: String) {
        this.password = password.trim()
        passwordChecker.check(password, passwordMatch)
        _passwordChecks.clear()
        _passwordChecks.addAll(passwordChecker.getChecks())
    }

    fun changePasswordMatch(passwordMatch: String) {
        this.passwordMatch = passwordMatch.trim()
        passwordChecker.checkMatch(password, passwordMatch)
        _passwordChecks[_passwordChecks.lastIndex] = passwordChecker.getChecks().last()
    }

    private class PasswordChecker {
        private val lowercaseRegex = Regex("[a-z]")
        private val uppercaseRegex = Regex("[A-Z]")
        private val characterRegex = Regex("[!@#\$%^&*()`~?,<.>]")
        private val digitRegex = Regex("\\d")
        private var lengthMinCheck = false
        private var lengthMaxCheck = true
        private var lowercaseCheck = false
        private var uppercaseCheck = false
        private var characterCheck = false
        private var digitCheck = false
        private var matchCheck = true

        fun check(
            password: String,
            passwordMatch: String
        ) {
            lengthMinCheck = password.length >= 8
            lengthMaxCheck = password.length < 30
            lowercaseCheck = password.contains(lowercaseRegex)
            uppercaseCheck = password.contains(uppercaseRegex)
            characterCheck = password.contains(characterRegex)
            digitCheck = password.contains(digitRegex)
            checkMatch(password, passwordMatch)
        }

        fun checkMatch(
            password: String,
            passwordMatch: String
        ) {
            matchCheck = password.isNotBlank() && password == passwordMatch
        }

        fun getChecks(): List<Pair<Boolean, Int>> {
            return listOf(
                lengthMinCheck to R.string.sign_up_pw_min_length,
                lengthMaxCheck to R.string.sign_up_pw_max_length,
                lowercaseCheck to R.string.sign_up_pw_lowercase,
                uppercaseCheck to R.string.sign_up_pw_uppercase,
                characterCheck to R.string.sign_up_pw_character,
                digitCheck to R.string.sign_up_pw_digit,
                matchCheck to R.string.sign_up_pw_match_error
            )
        }

        val isValidated =
            lengthMinCheck && lengthMaxCheck && lowercaseCheck &&
                uppercaseCheck && digitCheck && characterCheck && matchCheck
    }
}
