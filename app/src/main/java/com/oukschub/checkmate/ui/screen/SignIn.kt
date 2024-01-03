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
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
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
                .weight(.85F, true),
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
            modifier = Modifier.weight(.15F, true)
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
        OutlinedTextField(
            value = email,
            onValueChange = { onChangeEmail(it) },
            placeholder = { Text(text = stringResource(R.string.email)) }
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { onChangePassword(it) },
            placeholder = { Text(text = stringResource(R.string.password)) }
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
        modifier = Modifier,
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
