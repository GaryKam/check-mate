package com.oukschub.checkmate.ui.forgotpassword

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.EmailTextField
import com.oukschub.checkmate.util.MessageUtil

@Composable
fun ForgotPasswordScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailTextField(
            email = viewModel.email,
            errorMessage = stringResource(viewModel.emailError),
            focusManager = focusManager,
            onEmailChange = { viewModel.changeEmail(it) },
            modifier = Modifier.padding(top = 80.dp)
        )

        val context = LocalContext.current
        Button(
            onClick = {
                focusManager.clearFocus()
                viewModel.resetPassword(
                    onSuccess = {
                        onBack()
                        MessageUtil.displayToast(context, R.string.forgot_password_success)
                    },
                    onFailure = { MessageUtil.displayToast(context, R.string.forgot_password_failure) }
                )
            },
            modifier = Modifier.align(alignment = Alignment.End)
        ) {
            Text("Send")
        }
    }
}
