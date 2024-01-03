package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
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
import com.oukschub.checkmate.viewmodel.SignInViewModel

@Composable
fun SignIn(
    onSignIn: () -> Unit,
    onClickSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel()
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
                .weight(.85F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Logo()

            InputFields(
                email = email,
                password = password,
                onChangeEmail = { email = it },
                onChangePassword = { password = it },
            )

            Spacer(modifier = Modifier.height(10.dp))

            SignInButton(
                viewModel = viewModel,
                email = email,
                password = password,
                onSignIn = onSignIn
            )
        }

        Footer(
            onClickSignUp = onClickSignUp,
            modifier = Modifier
                .weight(.15F)
        )
    }
}

@Composable
private fun Logo() {
    Image(
        painter = painterResource(R.drawable.ic_launcher_background),
        contentDescription = null,
        modifier = Modifier
            .padding(30.dp)
            .size(200.dp)
            .border(BorderStroke(4.dp, MaterialTheme.colorScheme.primary), CircleShape)
            .clip(CircleShape)
    )
}

@Composable
private fun InputFields(
    email: String,
    password: String,
    onChangeEmail: (String) -> Unit,
    onChangePassword: (String) -> Unit,
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
private fun SignInButton(
    viewModel: SignInViewModel,
    email: String,
    password: String,
    onSignIn: () -> Unit,
) {
    val context = LocalContext.current

    Button(
        onClick = {
            viewModel.signIn(
                email = email,
                password = password,
                onSuccess = { onSignIn() },
                onFailure = { MessageUtil.displayToast(context, R.string.sign_in_failure) },
                onError = { MessageUtil.displayToast(context, R.string.sign_in_error) }
            )
        }
    ) {
        Text(text = stringResource(R.string.sign_in))
    }
}

@Composable
private fun Footer(
    onClickSignUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Divider(Modifier.padding(vertical = 30.dp))

        ClickableText(
            text = buildAnnotatedString {
                append(stringResource(R.string.sign_in_prompt_to_sign_up))
                append(" ")
                pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                append(stringResource(R.string.sign_up))
            },
            onClick = { onClickSignUp() }
        )
    }
}
