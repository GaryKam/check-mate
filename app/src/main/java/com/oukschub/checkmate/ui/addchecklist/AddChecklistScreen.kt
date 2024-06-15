package com.oukschub.checkmate.ui.addchecklist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist

/**
 * The screen to create a new checklist.
 */
@Composable
fun AddChecklistScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AddChecklistViewModel = hiltViewModel()
) {
    val focusManager = LocalFocusManager.current

    Box(contentAlignment = Alignment.Center) {
        Column(modifier = modifier.fillMaxSize()) {
            Scaffold(
                topBar = {
                    TopBar(
                        onBack = {
                            focusManager.clearFocus()
                            onBack()
                        },
                        onChecklistAdd = {
                            focusManager.clearFocus()
                            viewModel.addChecklist(onSuccess = { onSuccess() })
                        }
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
                        Checklist(
                            header = {
                                Header(
                                    title = viewModel.title,
                                    onTitleSet = { title -> viewModel.title = title }
                                )
                            },
                            items = viewModel.items,
                            onItemCheck = { itemIndex, isChecked -> viewModel.setItemChecked(itemIndex, isChecked) },
                            onItemNameFocus = {},
                            onItemNameChange = { itemIndex, itemName -> viewModel.setItemName(itemIndex, itemName) },
                            onItemNameSet = { _, _ -> },
                            onItemAdd = { itemName -> viewModel.addItem(itemName) },
                            onItemDelete = { itemIndex -> viewModel.deleteItem(itemIndex) },
                            onItemMove = { _, _ -> },
                            onItemMoveDone = {},
                            modifier = Modifier.padding(vertical = 20.dp)
                        )
                    }
                }
            }
        }

        AnimatedVisibility(visible = viewModel.isAddingChecklist) {
            CircularProgressIndicator(modifier = Modifier.padding(80.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBack: () -> Unit,
    onChecklistAdd: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(stringResource(R.string.add_checklist)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onChecklistAdd) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.desc_add_checklist)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

@Composable
private fun Header(
    title: String,
    onTitleSet: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = { onTitleSet(it) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 18.sp),
        placeholder = { Text(stringResource(R.string.checklist_title_placeholder)) },
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors()
    )
}
