package com.oukschub.checkmate.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.ChecklistItem

/**
 * A container that displays information associated to a checklist.
 */
@Composable
fun Checklist(
    header: @Composable () -> Unit,
    items: ImmutableList<ChecklistItem>,
    onItemCheck: (Int, Boolean) -> Unit,
    onItemNameFocus: (String) -> Unit,
    onItemNameChange: (Int, String) -> Unit,
    onItemNameSet: (Int, String) -> Unit,
    onItemAdd: (String) -> Unit,
    onItemDelete: (Int) -> Unit,
    onDividerCheck: (Int, Boolean) -> Unit = { _, _ -> },
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp)
            .pointerInput(Unit) {
                detectTapGestures(onTap = { focusManager.clearFocus() })
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        header()
        Checkboxes(
            items = items,
            onItemCheck = onItemCheck,
            onItemNameFocus = onItemNameFocus,
            onItemNameChange = onItemNameChange,
            onItemNameSet = onItemNameSet,
            onItemDelete = onItemDelete,
            onDividerCheck = onDividerCheck
        )
        InputField(onItemAdd = onItemAdd)
    }
}

@Composable
private fun Checkboxes(
    items: ImmutableList<ChecklistItem>,
    onItemCheck: (Int, Boolean) -> Unit,
    onItemNameFocus: (String) -> Unit,
    onItemNameChange: (Int, String) -> Unit,
    onItemNameSet: (Int, String) -> Unit,
    onItemDelete: (Int) -> Unit,
    onDividerCheck: (Int, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(horizontal = 20.dp)
    ) {
        for ((itemIndex, item) in items.withIndex()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
            ) {
                if (item.isDivider) {
                    ChecklistDivider(
                        checklistDivider = item,
                        itemIndex = itemIndex,
                        onDividerCheck = onDividerCheck
                    )
                } else {
                    Checkbox(
                        checked = item.isChecked,
                        onCheckedChange = { onItemCheck(itemIndex, it) }
                    )

                    BasicTextField(
                        value = item.name,
                        onValueChange = { onItemNameChange(itemIndex, it) },
                        modifier = Modifier
                            .weight(1.0F)
                            .onFocusChanged { focusState ->
                                if (focusState.isFocused) {
                                    onItemNameFocus(item.name)
                                } else {
                                    onItemNameSet(itemIndex, item.name)
                                }
                            },
                        enabled = !item.isChecked,
                        textStyle = TextStyle(
                            textDecoration = if (item.isChecked) {
                                TextDecoration.LineThrough
                            } else {
                                TextDecoration.None
                            }
                        )
                    )

                    IconButton(onClick = { onItemDelete(itemIndex) }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                    }
                }
            }
        }
    }
}

@Composable
private fun ChecklistDivider(
    checklistDivider: ChecklistItem,
    itemIndex: Int,
    onDividerCheck: (Int, Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = checklistDivider.name)
        Divider(modifier = Modifier.padding(horizontal = 10.dp))
        Checkbox(
            checked = checklistDivider.isChecked,
            onCheckedChange = {
                onDividerCheck(itemIndex, it)
            }
        )
    }
}

@Composable
private fun InputField(onItemAdd: (String) -> Unit) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(stringResource(R.string.type_placeholder)) },
        trailingIcon = {
            IconButton(onClick = {
                onItemAdd(text)
                text = ""
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.desc_done)
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
