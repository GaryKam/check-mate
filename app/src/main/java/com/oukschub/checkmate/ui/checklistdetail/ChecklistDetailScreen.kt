package com.oukschub.checkmate.ui.checklistdetail

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist

/**
 * The screen that displays all details related to an individual checklist.
 */
@Composable
fun ChecklistDetailScreen(
    checklistIndex: Int,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistDetailViewModel = hiltViewModel()
) {
    val checklist = viewModel.getChecklist(checklistIndex)
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (checklist != null) {
            Checklist(
                header = {
                    val dividerText = stringResource(R.string.checklist_default_divider)
                    Header(
                        title = checklist.title,
                        onTitleFocus = { title -> viewModel.focusTitle(title) },
                        onTitleSet = { title -> viewModel.setTitle(checklistIndex, title) },
                        onChecklistUnfavorite = { viewModel.unfavoriteChecklist(checklistIndex) },
                        onChecklistDelete = {
                            onDelete()
                            viewModel.deleteChecklist(checklistIndex)
                        },
                        onDividerAdd = { viewModel.addItem(checklistIndex, dividerText, true) }
                    )
                },
                items = ImmutableList.copyOf(checklist.items),
                onItemCheck = { itemIndex, isChecked ->
                    viewModel.setItemChecked(checklistIndex, itemIndex, isChecked)
                },
                onItemNameFocus = { itemName -> viewModel.focusItem(itemName) },
                onItemNameChange = { itemIndex, itemName ->
                    viewModel.changeItemName(checklistIndex, itemIndex, itemName)
                },
                onItemNameSet = { itemIndex, itemName -> viewModel.setItemName(checklistIndex, itemIndex, itemName) },
                onItemAdd = { itemName -> viewModel.addItem(checklistIndex, itemName) },
                onItemDelete = { itemIndex -> viewModel.deleteItem(checklistIndex, itemIndex) },
                onDividerCheck = { dividerIndex, isChecked ->
                    viewModel.setDividerChecked(checklistIndex, dividerIndex, isChecked)
                }
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    onTitleFocus: (String) -> Unit,
    onTitleSet: (String) -> Unit,
    onChecklistUnfavorite: () -> Unit,
    onChecklistDelete: () -> Unit,
    onDividerAdd: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 20.dp, top = 5.dp, end = 5.dp)
    ) {
        var checklistTitle by remember { mutableStateOf(title) }
        TextField(
            value = checklistTitle,
            onValueChange = { checklistTitle = it },
            textStyle = TextStyle(fontSize = 18.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onTitleFocus(checklistTitle)
                } else {
                    onTitleSet(checklistTitle)
                }
            }
        )

        Box {
            var isDropdownVisible by remember { mutableStateOf(false) }

            IconButton(onClick = { isDropdownVisible = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.desc_checklist_options)
                )
            }

            DropdownMenu(
                expanded = isDropdownVisible,
                onDismissRequest = { isDropdownVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_unfavorite)) },
                    onClick = {
                        isDropdownVisible = false
                        onChecklistUnfavorite()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_delete)) },
                    onClick = {
                        isDropdownVisible = false
                        onChecklistDelete()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_add_divider)) },
                    onClick = {
                        isDropdownVisible = false
                        onDividerAdd()
                    }
                )
            }
        }
    }
}