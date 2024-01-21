package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun Home(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { focusManager.clearFocus() }
            ),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for ((checklistIndex, checklist) in viewModel.checklists.withIndex()) {
            Checklist(
                header = {
                    Header(
                        title = checklist.title,
                        onTitleFocus = { viewModel.focusChecklistTitle(it) },
                        onTitleChange = { viewModel.changeChecklistTitle(checklistIndex, it) },
                        onTitleUpdate = { title ->
                            viewModel.updateChecklistTitle(
                                checklistIndex,
                                title
                            )
                        },
                        onChecklistRemoveFavorite = {
                            viewModel.updateChecklistFavorite(
                                checklistIndex
                            )
                        },
                        onChecklistDelete = { viewModel.deleteChecklist(checklist) }
                    )
                },
                items = ImmutableList.copyOf(checklist.items),
                onItemChange = { itemIndex, name, isChecked ->
                    viewModel.changeChecklistItem(checklistIndex, itemIndex, name, isChecked)
                    viewModel.updateChecklistItem(checklistIndex)
                },
                onItemCreate = {}
            )
        }
    }
}

@Composable
private fun Header(
    title: String,
    onTitleFocus: (String) -> Unit,
    onTitleChange: (String) -> Unit,
    onTitleUpdate: (String) -> Unit,
    onChecklistRemoveFavorite: () -> Unit,
    onChecklistDelete: () -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 5.dp, end = 5.dp)
    ) {
        TextField(
            value = title,
            onValueChange = { onTitleChange(it) },
            textStyle = TextStyle(fontSize = 18.sp),
            colors = TextFieldDefaults.colors(
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier.onFocusChanged { focusState ->
                if (focusState.isFocused) {
                    onTitleFocus(title)
                } else {
                    onTitleUpdate(title)
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
                DropdownMenuItem(text = {
                    Text(stringResource(R.string.checklist_unfavorite))
                }, onClick = {
                    isDropdownVisible = false
                    onChecklistRemoveFavorite()
                })

                DropdownMenuItem(text = {
                    Text(stringResource(R.string.checklist_delete))
                }, onClick = {
                    isDropdownVisible = false
                    onChecklistDelete()
                })
            }
        }
    }
}
