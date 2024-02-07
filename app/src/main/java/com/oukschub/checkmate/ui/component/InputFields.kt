package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.oukschub.checkmate.R

/**
 * A combination of a [DisplayNameTextField] above a [PasswordTextField].
 */
@Composable
fun InputFields(
    email: String,
    password: String,
    passwordImeAction: ImeAction,
    emailError: String,
    passwordError: String,
    focusManager: FocusManager,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        EmailTextField(
            email = email,
            errorMessage = emailError,
            focusManager = focusManager,
            onEmailChange = onEmailChange
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            password = password,
            imeAction = passwordImeAction,
            errorMessage = passwordError,
            placeholder = stringResource(R.string.password),
            onPasswordChange = onPasswordChange,
            onImeAction = { onImeAction() }
        )
    }
}

@Composable
private fun EmailTextField(
    email: String,
    errorMessage: String,
    focusManager: FocusManager,
    onEmailChange: (String) -> Unit
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        placeholder = { Text(stringResource(R.string.email)) },
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(errorMessage)
            }
        },
        isError = errorMessage.isNotBlank(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true
    )
}
