package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.firebase.ui.auth.R

@Composable
fun PasswordTextField(
    password: String,
    errorMessage: String,
    placeholder: String,
    focusManager: FocusManager,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val passwordVisualTransformation = PasswordVisualTransformation()
    var isPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = { onPasswordChange(it) },
        modifier = modifier,
        placeholder = { Text(placeholder) },
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible },
                modifier = Modifier.focusProperties { canFocus = false }
            ) {
                val resId = if (isPasswordVisible) {
                    R.drawable.design_ic_visibility
                } else {
                    R.drawable.design_ic_visibility_off
                }

                Icon(
                    painter = painterResource(resId),
                    contentDescription = null
                )
            }
        },
        supportingText = {
            if (errorMessage.isNotBlank()) {
                Text(errorMessage)
            }
        },
        isError = errorMessage.isNotBlank(),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else passwordVisualTransformation,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
        keyboardActions = KeyboardActions(onSend = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true
    )
}
