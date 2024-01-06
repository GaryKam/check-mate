package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation

@Composable
fun PasswordTextField(
    password: String,
    errorMessage: String,
    placeholder: String,
    focusManager: FocusManager,
    onChangePassword: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordVisualTransformation = PasswordVisualTransformation()

    var isPasswordVisible by remember {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = password,
        onValueChange = { onChangePassword(it) },
        modifier = modifier,
        placeholder = { Text(text = placeholder) },
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier.focusProperties { canFocus = false }
            ) {
                Icon(
                    imageVector = if (isPasswordVisible) Icons.Default.Search else Icons.Default.Lock,
                    contentDescription = null
                )
            }
        },
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(text = errorMessage)
            }
        },
        isError = errorMessage.isNotBlank(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else passwordVisualTransformation,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true
    )
}
