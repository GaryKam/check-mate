package com.oukschub.checkmate.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist

/**
 * The screen displayed after Splash screen completes.
 * Displays the user's favorite checklists.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.isContentVisible = true
    }

    if (viewModel.checklists.none { it.isFavorite }) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.ic_checkmate_sad),
                contentDescription = null,
                modifier = Modifier.scale(0.8F),
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
            )

            Text(text = stringResource(R.string.home_no_favorites))
        }
    } else {
        LazyColumn(modifier = modifier.fillMaxSize()) {
            itemsIndexed(items = viewModel.checklists) { checklistIndex, checklist ->
                if (!checklist.isFavorite) {
                    return@itemsIndexed
                }

                AnimatedVisibility(
                    visible = viewModel.isContentVisible,
                    enter = fadeIn(tween(200, 80 * checklistIndex)) +
                        slideInVertically(
                            tween(200, 80 * checklistIndex),
                            initialOffsetY = { it / 2 }
                        )
                ) {
                    Checklist(
                        header = {
                            Header(
                                title = checklist.title,
                                onTitleFocus = { title -> viewModel.focusChecklistTitle(title) },
                                onTitleChange = { title -> viewModel.changeChecklistTitle(checklistIndex, title) },
                                onTitleSet = { title -> viewModel.setChecklistTitle(checklistIndex, title) },
                                onChecklistUnfavorite = { viewModel.unfavoriteChecklist(checklistIndex) },
                                onChecklistDelete = { viewModel.deleteChecklist(checklistIndex) }
                            )
                        },
                        items = ImmutableList.copyOf(checklist.items),
                        onItemSet = { itemIndex, itemName, isChecked ->
                            viewModel.setChecklistItem(checklistIndex, itemIndex, itemName, isChecked)
                        },
                        onItemAdd = { itemName ->
                            viewModel.addChecklistItem(checklistIndex, itemName)
                        },
                        onItemLongClick = { itemIndex ->
                            viewModel.showDeleteChecklistItemDialog(checklistIndex, itemIndex)
                        }
                    )
                }
            }
        }

        if (viewModel.isRemoveChecklistItemDialogVisible) {
            AlertDialog(
                onDismissRequest = { viewModel.hideDeleteChecklistItemDialog() },
                confirmButton = {
                    Button(onClick = { viewModel.deleteChecklistItem() }) {
                        Text(text = stringResource(R.string.confirm))
                    }
                },
                dismissButton = {
                    Button(onClick = { viewModel.hideDeleteChecklistItemDialog() }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                },
                title = { Text(text = stringResource(R.string.home_delete_dialog_title)) },
                text = { Text(text = stringResource(R.string.home_delete_dialog_prompt, viewModel.itemToBeDeleted)) }
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    onTitleFocus: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onTitleSet: (String) -> Unit,
    onChecklistUnfavorite: () -> Unit,
    onChecklistDelete: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 20.dp, top = 5.dp, end = 5.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(fontSize = 18.sp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onTitleFocus(title)
                } else {
                    onTitleSet(title)
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
            }
        }
    }
}
