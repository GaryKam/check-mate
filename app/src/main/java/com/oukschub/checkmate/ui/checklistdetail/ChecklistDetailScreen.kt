package com.oukschub.checkmate.ui.checklistdetail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.oukschub.checkmate.ui.component.DeleteChecklistDialog

/**
 * The screen that displays all details related to an individual checklist.
 */
@Composable
fun ChecklistDetailScreen(
    checklistIndex: Int,
    onBack: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistDetailViewModel = hiltViewModel()
) {
    val checklist = viewModel.getChecklist(checklistIndex)
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxSize()) {
        Scaffold(
            Modifier.fillMaxSize(),
            topBar = {
                val dividerText = stringResource(R.string.checklist_default_divider)
                TopBar(
                    isEditing = viewModel.isEditingChecklist,
                    onBack = {
                        focusManager.clearFocus()
                        onBack()
                    },
                    onChecklistEdit = { viewModel.editChecklist() },
                    onChecklistPromptDelete = { viewModel.promptDeleteChecklist() },
                    onDividerAdd = { viewModel.addItem(checklistIndex, dividerText, true) }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(paddingValues)
                    .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier
                        .padding(top = 10.dp)
                        .fillMaxSize(),
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
                ) {
                    if (checklist != null) {
                        Checklist(
                            header = {
                                Header(
                                    title = checklist.title,
                                    onTitleFocus = { title ->
                                        viewModel.focusTitle(
                                            checklistIndex,
                                            title
                                        )
                                    },
                                    onTitleSet = { title ->
                                        viewModel.setTitle(
                                            checklistIndex,
                                            title
                                        )
                                    },
                                )
                            },
                            items = ImmutableList.copyOf(checklist.items),
                            onItemCheck = { itemIndex, isChecked ->
                                viewModel.setItemChecked(
                                    checklistIndex,
                                    itemIndex,
                                    isChecked
                                )
                            },
                            onItemNameFocus = { itemName ->
                                viewModel.focusItem(
                                    checklistIndex,
                                    itemName
                                )
                            },
                            onItemNameChange = { itemIndex, itemName ->
                                viewModel.changeItemName(
                                    checklistIndex,
                                    itemIndex,
                                    itemName
                                )
                            },
                            onItemNameSet = { itemIndex, itemName ->
                                viewModel.setItemName(
                                    checklistIndex,
                                    itemIndex,
                                    itemName
                                )
                            },
                            onItemAdd = { itemName -> viewModel.addItem(checklistIndex, itemName) },
                            onItemDelete = { itemIndex ->
                                viewModel.deleteItem(
                                    checklistIndex,
                                    itemIndex
                                )
                            },
                            onItemMove = { fromIndex, toIndex ->
                                viewModel.moveItem(
                                    checklistIndex,
                                    fromIndex,
                                    toIndex
                                )
                            },
                            onItemMoveDone = { viewModel.finishMovingItem(checklistIndex) },
                            modifier = Modifier.padding(vertical = 20.dp),
                            isEditing = viewModel.isEditingChecklist
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = viewModel.isDeletePromptVisible) {
            DeleteChecklistDialog(
                onDismiss = { viewModel.hideDeleteChecklistDialog() },
                onConfirm = {
                    onDelete()
                    viewModel.hideDeleteChecklistDialog()
                    viewModel.deleteChecklist(checklistIndex)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    isEditing: Boolean,
    onBack: () -> Unit,
    onChecklistEdit: () -> Unit,
    onChecklistPromptDelete: () -> Unit,
    onDividerAdd: () -> Unit
) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back)
                )
            }
        },
        actions = {
            Box {
                var isMenuVisible by remember { mutableStateOf(false) }

                IconButton(onClick = { isMenuVisible = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.desc_checklist_detail_menu)
                    )
                }

                DropdownMenu(
                    expanded = isMenuVisible,
                    onDismissRequest = { isMenuVisible = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(if (isEditing) R.string.checklist_edit_stop else R.string.checklist_edit)) },
                        onClick = {
                            isMenuVisible = false
                            onChecklistEdit()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.checklist_delete)) },
                        onClick = {
                            isMenuVisible = false
                            onChecklistPromptDelete()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.checklist_add_divider)) },
                        onClick = {
                            isMenuVisible = false
                            onDividerAdd()
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun Header(
    title: String,
    onTitleFocus: (String) -> Unit,
    onTitleSet: (String) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.Start,
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
    }
}
