package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
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
import com.oukschub.checkmate.R

@Composable
fun EmailTextField(
    email: String,
    errorMessage: String,
    focusManager: FocusManager,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = { onEmailChange(it) },
        modifier = modifier.fillMaxWidth(),
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
