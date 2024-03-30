package com.oukschub.checkmate.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.oukschub.checkmate.R

/**
 * The screen for users to update their account information and logout.
 */
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    onSignOut: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Card(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            OutlinedCard(
                modifier = Modifier
                    .weight(1.0F)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 10.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 30.dp, vertical = 10.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val displayNameState = viewModel.displayName.collectAsState(initial = "")

                    ProfileIcon()
                    ProfileName(name = displayNameState.value)
                    ChangeDisplayNameButton(
                        displayName = displayNameState.value,
                        onDisplayNameSet = { displayName -> viewModel.setNewDisplayName(displayName) }
                    )
                    ChangePasswordButton()
                    SettingsButton()
                    SignOutButton(
                        onSignOut = {
                            viewModel.signOut()
                            onSignOut()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileIcon() {
    Image(
        imageVector = Icons.Default.AccountCircle,
        contentDescription = null,
        modifier = Modifier.size(100.dp),
        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
    )
}

@Composable
private fun ProfileName(name: String) {
    Text(
        text = name,
        modifier = Modifier.padding(bottom = 5.dp),
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.Medium,
        style = MaterialTheme.typography.titleLarge
    )
}

@Composable
private fun ChangeDisplayNameButton(
    displayName: String,
    onDisplayNameSet: (String) -> Unit
) {
    var showChangeUsernameDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showChangeUsernameDialog = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.profile_change_display_name))
    }

    if (showChangeUsernameDialog) {
        ChangeDisplayNameDialog(
            currentDisplayName = displayName,
            onDismiss = { showChangeUsernameDialog = false },
            onConfirm = { name -> onDisplayNameSet(name) }
        )
    }
}

@Composable
private fun ChangePasswordButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.profile_change_password))
    }
}

@Composable
private fun SettingsButton() {
    Button(
        onClick = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.profile_settings))
    }
}

@Composable
private fun SignOutButton(onSignOut: () -> Unit) {
    var showSignOutDialog by remember { mutableStateOf(false) }

    Button(
        onClick = { showSignOutDialog = true },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(stringResource(R.string.profile_sign_out))
    }

    if (showSignOutDialog) {
        SignOutDialog(
            onDismiss = { showSignOutDialog = false },
            onConfirm = { onSignOut() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangeDisplayNameDialog(
    currentDisplayName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    AlertDialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(20.dp)
        ) {
            Text(stringResource(R.string.profile_change_display_name))

            var displayName by remember { mutableStateOf(currentDisplayName) }
            TextField(
                value = displayName,
                onValueChange = { displayName = it },
                modifier = Modifier.padding(vertical = 10.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Row(
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Text(stringResource(R.string.cancel))
                }

                TextButton(
                    onClick = {
                        onDismiss()
                        onConfirm(displayName)
                    }
                ) {
                    Text(stringResource(R.string.confirm))
                }
            }
        }
    }
}

@Composable
private fun SignOutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDismiss()
                    onConfirm()
                }
            ) {
                Text(stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
        text = { Text(stringResource(R.string.profile_prompt_sign_out)) }
    )
}
