package com.oukschub.checkmate.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.viewmodel.SignInViewModel

@Composable
fun SignIn(
    onSignIn: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignInViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(modifier = modifier.fillMaxSize()) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = { Text(text = "Email") }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text(text = "Password") }
        )

        val context = LocalContext.current
        Button(onClick = {
            viewModel.signIn(
                email = email,
                password = password,
                onSuccess = { onSignIn() },
                onFailure = { Toast.makeText(context, "Invalid username or password", Toast.LENGTH_SHORT).show() },
                onError = { Toast.makeText(context, "Please fill in empty textfields", Toast.LENGTH_SHORT).show() }
            )}
        ) {
            Text(text = "Sign In")
        }
    }
}