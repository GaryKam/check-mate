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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.ui.component.Footer
import com.oukschub.checkmate.util.MessageUtil

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
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    Box(contentAlignment = Alignment.Center) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
        ) {
            Scaffold(
                topBar = {
                    TopBar(
                        onBack = {
                            focusManager.clearFocus()
                            onBack()
                        }
                    )
                }
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(paddingValues)
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
                        AnimatedVisibility(visible = viewModel.isCreatingNewChecklist) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
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

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        viewModel.addChecklist(
                                            onSuccess = {
                                                onSuccess()
                                                MessageUtil.displayToast(context, it)
                                            }
                                        )
                                    }
                                ) {
                                    Text(stringResource(R.string.add_checklist_create_checklist))
                                }

                                Footer(
                                    text = buildAnnotatedString {
                                        append(stringResource(R.string.add_checklist_share_prompt))
                                        append(" ")
                                        pushStyle(SpanStyle(color = Color.Blue, fontWeight = FontWeight.Bold))
                                        append(stringResource(R.string.click_here))
                                    },
                                    onClick = { viewModel.setCreationType(1) },
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                        }

                        AnimatedVisibility(visible = !viewModel.isCreatingNewChecklist) {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                OutlinedTextField(
                                    value = viewModel.sharedChecklistCode,
                                    onValueChange = { viewModel.setShareCode(it.toUpperCase(Locale.current)) },
                                    modifier = Modifier.padding(vertical = 20.dp),
                                    label = { Text(stringResource(R.string.add_checklist_shared_code)) }
                                )

                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        viewModel.addSharedChecklist(
                                            onSuccess = {
                                                onSuccess()
                                                MessageUtil.displayToast(context, it)
                                            },
                                            onFailure = { MessageUtil.displayToast(context, it) }
                                        )
                                    },
                                    enabled = viewModel.sharedChecklistCode.length == 6
                                ) {
                                    Text(stringResource(R.string.add_checklist_shared_checklist))
                                }
                            }
                        }
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
private fun TopBar(onBack: () -> Unit) {
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
