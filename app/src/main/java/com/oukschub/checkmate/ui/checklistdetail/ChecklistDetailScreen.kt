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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
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
    onBack: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChecklistDetailViewModel = hiltViewModel()
) {
    val checklist = viewModel.getChecklist(checklistIndex)
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            val dividerText = stringResource(R.string.checklist_default_divider)
            TopBar(
                isFavorite = checklist?.isFavorite ?: false,
                onBack = {
                    focusManager.clearFocus()
                    onBack()
                },
                onChecklistUnfavorite = { viewModel.unfavoriteChecklist(checklistIndex) },
                onChecklistDelete = {
                    onDelete()
                    viewModel.deleteChecklist(checklistIndex)
                },
                onChecklistClear = { viewModel.clearChecklist(checklistIndex) },
                onDividerAdd = { viewModel.addItem(checklistIndex, dividerText, true) }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (checklist != null) {
                Checklist(
                    header = {
                        Header(
                            title = checklist.title,
                            onTitleFocus = { title -> viewModel.focusTitle(title) },
                            onTitleSet = { title -> viewModel.setTitle(checklistIndex, title) },
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
                    onItemNameSet = { itemIndex, itemName ->
                        viewModel.setItemName(checklistIndex, itemIndex, itemName)
                    },
                    onItemAdd = { itemName -> viewModel.addItem(checklistIndex, itemName) },
                    onItemDelete = { itemIndex -> viewModel.deleteItem(checklistIndex, itemIndex) },
                    onDividerCheck = { dividerIndex, isChecked ->
                        viewModel.setDividerChecked(checklistIndex, dividerIndex, isChecked)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    isFavorite: Boolean,
    onBack: () -> Unit,
    onChecklistUnfavorite: () -> Unit,
    onChecklistDelete: () -> Unit,
    onChecklistClear: () -> Unit,
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
                    if (isFavorite) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.checklist_unfavorite)) },
                            onClick = {
                                isMenuVisible = false
                                onChecklistUnfavorite()
                            }
                        )
                    }

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.checklist_delete)) },
                        onClick = {
                            isMenuVisible = false
                            onChecklistDelete()
                        }
                    )

                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.checklist_clear)) },
                        onClick = {
                            isMenuVisible = false
                            onChecklistClear()
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
        }
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
