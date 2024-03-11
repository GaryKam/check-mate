package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailTextField(
            email = email,
            errorMessage = emailError,
            focusManager = focusManager,
            onEmailChange = onEmailChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        PasswordTextField(
            password = password,
            imeAction = passwordImeAction,
            errorMessage = passwordError,
            placeholder = stringResource(R.string.password),
            onPasswordChange = onPasswordChange,
            onImeAction = { onImeAction() },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
