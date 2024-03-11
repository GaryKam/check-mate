package com.oukschub.checkmate.ui.signup

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.DisplayNameTextField
import com.oukschub.checkmate.ui.component.Footer
import com.oukschub.checkmate.ui.component.InputFields
import com.oukschub.checkmate.ui.component.PasswordTextField
import com.oukschub.checkmate.util.MessageUtil

/**
 * The screen for users to create a new account.
 */
@Composable
fun SignUpScreen(
    onSignUp: () -> Unit,
    onFooterClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(40.dp)
                .weight(0.85F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DisplayNameTextField(
                displayName = viewModel.displayName,
                errorIds = viewModel.displayNameErrors,
                focusManager = focusManager,
                onDisplayNameChange = { viewModel.changeDisplayName(it) },
                modifier = Modifier.fillMaxWidth()
            )

            InputFields(
                email = viewModel.email,
                password = viewModel.password,
                passwordImeAction = ImeAction.Next,
                emailError = stringResource(viewModel.emailError),
                passwordError = "",
                focusManager = focusManager,
                onEmailChange = { viewModel.changeEmail(it) },
                onPasswordChange = { viewModel.changePassword(it) },
                onImeAction = { focusManager.moveFocus(FocusDirection.Down) },
                modifier = Modifier.fillMaxWidth()
            )

            val context = LocalContext.current

            PasswordTextField(
                password = viewModel.passwordMatch,
                imeAction = ImeAction.Done,
                errorMessage = "",
                placeholder = stringResource(R.string.sign_up_repeat_password),
                onPasswordChange = { viewModel.changePasswordMatch(it) },
                onImeAction = {
                    viewModel.signUp(
                        onSuccess = { onSignUp() },
                        onFailure = { MessageUtil.displayToast(context, it) }
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )

            PasswordCheckText(passwordChecks = viewModel.passwordChecks)

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                modifier = Modifier,
                onClick = {
                    focusManager.clearFocus()
                    viewModel.signUp(
                        onSuccess = { onSignUp() },
                        onFailure = { MessageUtil.displayToast(context, it) }
                    )
                }
            ) {
                Text(stringResource(R.string.sign_up))
            }

            if (viewModel.isSigningUp) {
                CircularProgressIndicator(modifier = Modifier.padding(80.dp))
            }
        }

        Footer(
            text = buildAnnotatedString {
                append(stringResource(R.string.sign_up_prompt_to_sign_in))
                append(" ")
                pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                append(stringResource(R.string.sign_in))
            },
            onClick = { onFooterClick() },
            modifier = Modifier.weight(0.15F)
        )
    }
}

@Composable
private fun PasswordCheckText(passwordChecks: ImmutableList<Pair<Boolean, Int>>) {
    Text(
        text = buildAnnotatedString {
            for ((checkStatus, resId) in passwordChecks) {
                if (checkStatus) {
                    pushStyle(SpanStyle(color = Color.Gray))
                    append(stringResource(resId))
                    pop()
                } else {
                    append(stringResource(R.string.red_x))
                    append(" ")
                    append(stringResource(resId))
                }
            }
        },
        fontSize = 12.sp
    )
}
