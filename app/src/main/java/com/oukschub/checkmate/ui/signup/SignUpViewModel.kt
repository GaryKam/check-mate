package com.oukschub.checkmate.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    var displayName by mutableStateOf("")
        private set
    var email by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set
    var passwordMatch by mutableStateOf("")
        private set
    private val _displayNameErrors = mutableStateListOf<Int>()
    val displayNameErrors: ImmutableList<Int>
        get() = ImmutableList.copyOf(_displayNameErrors)
    var emailError by mutableIntStateOf(R.string.blank)
        private set
    var isSigningUp by mutableStateOf(false)
        private set
    private val _passwordChecks = mutableStateListOf<Pair<Boolean, Int>>()
    val passwordChecks: ImmutableList<Pair<Boolean, Int>>
        get() = ImmutableList.copyOf(_passwordChecks)
    private val emailRegex = Regex("^\\S+@\\S+\\.\\S+$")
    private val displayNameChecker = DisplayNameChecker()
    private val passwordChecker = PasswordChecker()

    init {
        _passwordChecks.addAll(passwordChecker.getChecks())
    }

    fun signUp(
        onSuccess: () -> Unit,
        onFailure: (Int) -> Unit
    ) {
        if (isSigningUp) {
            return
        }

        displayNameChecker.check(displayName)

        val validEmail = emailRegex.matches(email)

        if (displayNameChecker.isValidated() && validEmail && passwordChecker.isValidated()) {
            isSigningUp = true
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Timber.d("Created user in FirebaseAuth: $email")
                    viewModelScope.launch {
                        if (userRepository.createUser(displayName.trim())) {
                            Timber.d("Created user in Firestore: $email")
                            isSigningUp = false
                            onSuccess()
                        } else {
                            Timber.d("Failed to create user in Firestore: $email")
                            onFailure(R.string.sign_up_failure)
                            isSigningUp = false
                            userRepository.signOut()
                        }
                    }
                }
                .addOnFailureListener {
                    Timber.d("Failed to create user in FirebaseAuth: $email")
                    onFailure(R.string.sign_up_failure)
                    isSigningUp = false
                }
        } else {
            _displayNameErrors.clear()
            for ((checkStatus, errorMessageId) in displayNameChecker.getChecks()) {
                if (!checkStatus) {
                    _displayNameErrors.add(errorMessageId)
                }
            }

            if (!validEmail) {
                emailError = R.string.sign_up_email_error
            }
        }
    }

    fun changeDisplayName(displayName: String) {
        this.displayName = displayName
        _displayNameErrors.clear()
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

    private class DisplayNameChecker {
        var lengthMinCheck = false
            private set
        var lengthMaxCheck = true
            private set
        var alphaNumCheck = false
            private set
        private val alphaNumRegex = Regex("^[a-zA-Z0-9\\s]+\$")

        fun check(displayName: String) {
            lengthMinCheck = displayName.length >= 2
            lengthMaxCheck = displayName.length < 20
            alphaNumCheck = displayName.matches(alphaNumRegex)
        }

        fun isValidated(): Boolean {
            return lengthMinCheck && lengthMaxCheck && alphaNumCheck
        }

        fun getChecks(): List<Pair<Boolean, Int>> {
            return listOf(
                lengthMinCheck to R.string.sign_up_display_name_min_length,
                lengthMaxCheck to R.string.sign_up_display_name_max_length,
                alphaNumCheck to R.string.sign_up_display_name_alpha_num
            )
        }
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

        fun isValidated(): Boolean {
            return lengthMinCheck && lengthMaxCheck && lowercaseCheck &&
                uppercaseCheck && digitCheck && characterCheck && matchCheck
        }
    }
}
