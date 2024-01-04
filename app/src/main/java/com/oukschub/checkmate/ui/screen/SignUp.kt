package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Footer
import com.oukschub.checkmate.ui.component.InputFields
import com.oukschub.checkmate.viewmodel.SignUpViewModel

@Composable
fun SignUp(
    onSignUp: () -> Unit,
    onClickSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignUpViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .weight(.85F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputFields(
                email = viewModel.email,
                password = viewModel.password,
                emailError = viewModel.emailError,
                passwordError = viewModel.passwordError,
                onChangeEmail = { viewModel.updateEmail(it) },
                onChangePassword = { viewModel.updatePassword(it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = {
                viewModel.signUp(
                    onSuccess = { onSignUp() }
                )
            }) {
                Text(text = stringResource(R.string.sign_up))
            }
        }

        Footer(
            text = buildAnnotatedString {
                append(stringResource(R.string.sign_up_prompt_to_sign_in))
                append(" ")
                pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                append(stringResource(R.string.sign_in))
            },
            onClickText = { onClickSignIn() },
            modifier = Modifier.weight(.15F)
        )
    }
}
