package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Footer
import com.oukschub.checkmate.ui.component.InputFields
import com.oukschub.checkmate.ui.component.Logo
import com.oukschub.checkmate.util.MessageUtil
import com.oukschub.checkmate.viewmodel.SignInViewModel

@Composable
fun SignIn(
    onNavigateToHome: () -> Unit,
    onNavigateToSignUp: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel()
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
            Logo(
                modifier = Modifier
                    .padding(30.dp)
                    .size(200.dp)
            )

            InputFields(
                email = viewModel.email,
                password = viewModel.password,
                emailError = stringResource(viewModel.emailError),
                passwordError = stringResource(viewModel.passwordError),
                focusManager = LocalFocusManager.current,
                onEmailChange = { viewModel.changeEmail(it) },
                onPasswordChange = { viewModel.changePassword(it) }
            )

            Spacer(modifier = Modifier.height(10.dp))

            val context = LocalContext.current
            Button(onClick = {
                viewModel.signIn(
                    onSuccess = { onNavigateToHome() },
                    onFailure = { MessageUtil.displayToast(context, it) }
                )
            }) {
                Text(text = stringResource(R.string.sign_in))
            }
        }

        Footer(
            text = buildAnnotatedString {
                append(stringResource(R.string.sign_in_prompt_to_sign_up))
                append(" ")
                pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                append(stringResource(R.string.sign_up))
            },
            onClick = { onNavigateToSignUp() },
            modifier = Modifier.weight(.15F)
        )
    }
}
