package com.oukschub.checkmate.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
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
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = stringResource(R.string.email)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = stringResource(R.string.password)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        val context = LocalContext.current
        Button(
            onClick = {
                viewModel.signIn(
                    email = email,
                    password = password,
                    onSuccess = { onSignIn() },
                    onFailure = {
                        Toast.makeText(
                            context,
                            R.string.sign_in_failure,
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onError = {
                        Toast.makeText(
                            context,
                            R.string.sign_in_error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }
        ) {
            Text(text = stringResource(R.string.sign_in))
        }

        Divider(Modifier.padding(vertical = 50.dp))

        ClickableText(
            text = AnnotatedString(text = stringResource(R.string.sign_in_prompt_to_sign_up)),
            onClick = { onClickSignUp() }
        )
    }
}
