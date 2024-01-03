package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.util.MessageUtil
import com.oukschub.checkmate.viewmodel.SignUpViewModel

@Composable
fun SignUp(
    onSignUp: () -> Unit,
    onClickSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(.85F, true),
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            InputFields(
                email = email,
                password = password,
                onChangeEmail = { email = it },
                onChangePassword = { password = it }
            )

            Spacer(modifier = Modifier.height(10.dp))

            SignUpButton(
                viewModel = viewModel,
                email = email,
                password = password,
                onSignUp = onSignUp
            )
        }

        Footer(
            onClickSignIn = onClickSignIn,
            modifier = Modifier.weight(.15F, true)
        )
    }
}

@Composable
private fun InputFields(
    email: String,
    password: String,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit
) {
    Column {
        val focusManager = LocalFocusManager.current

        OutlinedTextField(
            value = email,
            onValueChange = { onChangeEmail(it) },
            placeholder = { Text(text = stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(10.dp))

        val passwordVisualTransformation = PasswordVisualTransformation()

        var isVisible by remember {
            mutableStateOf(false)
        }

        OutlinedTextField(
            value = password,
            onValueChange = { onChangePassword(it) },
            placeholder = { Text(text = stringResource(R.string.password)) },
            visualTransformation = if (isVisible) VisualTransformation.None else passwordVisualTransformation,
            trailingIcon = {
                IconButton(onClick = {
                    isVisible = !isVisible
                },
                    modifier = Modifier.focusProperties { canFocus = false }
                ) {
                    Icon(
                        imageVector = if (isVisible) Icons.Default.Search else Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                // sends data on press enter while focused on password text field
                onSend = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            singleLine = true
        )
    }
}

@Composable
private fun SignUpButton(
    viewModel: SignUpViewModel,
    email: String,
    password: String,
    onSignUp: () -> Unit
) {
    val context = LocalContext.current

    Button(
        onClick = {
            viewModel.signUp(
                email = email,
                password = password,
                onSuccess = { onSignUp() },
                onFailure = { MessageUtil.displayToast(context, R.string.sign_up_failure) },
                onError = { MessageUtil.displayToast(context, R.string.sign_up_error) }
            )
        }
    ) {
        Text(text = stringResource(R.string.sign_up))
    }
}

@Composable
private fun Footer(
    onClickSignIn: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(Modifier.padding(vertical = 30.dp))

        ClickableText(
            text = buildAnnotatedString {
                append(stringResource(R.string.sign_up_prompt_to_sign_in))
                append(" ")
                pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                append(stringResource(R.string.sign_in))
            },
            onClick = { onClickSignIn() }
        )
    }
}
