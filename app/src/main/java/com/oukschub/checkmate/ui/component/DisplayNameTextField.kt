package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R

@Composable
fun DisplayNameTextField(
    displayName: String,
    errorIds: ImmutableList<Int>,
    focusManager: FocusManager,
    onDisplayNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var errorMessage = ""
    for (id in errorIds) {
        errorMessage += stringResource(id)
    }

    OutlinedTextField(
        value = displayName,
        onValueChange = { onDisplayNameChange(it) },
        placeholder = { Text(text = stringResource(R.string.sign_up_display_name)) },
        supportingText = { Text(text = errorMessage) },
        isError = errorIds.isNotEmpty(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onNext = { focusManager.moveFocus(FocusDirection.Down) }),
        singleLine = true,
        modifier = modifier
    )
}
