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
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.firebase.ui.auth.R

/**
 * A text field to input a password.
 */
@Composable
fun PasswordTextField(
    password: String,
    imeAction: ImeAction,
    errorMessage: String,
    placeholder: String,
    onPasswordChange: (String) -> Unit,
    onImeAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var isPasswordVisible by remember { mutableStateOf(false) }
    val passwordVisualTransformation = PasswordVisualTransformation()

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
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onAny = { onImeAction() }),
        singleLine = true
    )
}
