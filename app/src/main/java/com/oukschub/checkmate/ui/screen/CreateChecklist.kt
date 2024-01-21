package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.CreateChecklistViewModel

@Composable
fun CreateChecklist(
    viewModel: CreateChecklistViewModel,
    onBack: () -> Unit,
    onChecklistCreate: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                onBack = onBack,
                onChecklistCreate = { viewModel.createChecklist { onChecklistCreate() } }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Checklist(
                header = {
                    Header(
                        title = viewModel.title,
                        onTitleChange = { viewModel.changeChecklistTitle(it) }
                    )
                },
                items = viewModel.items,
                onItemChange = { index, name, isChecked ->
                    viewModel.changeChecklistItem(index, name, isChecked)
                },
                onItemCreate = { viewModel.addChecklistItem(it) },
            )

            if (viewModel.isCreatingChecklist) {
                CircularProgressIndicator()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBack: () -> Unit,
    onChecklistCreate: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(R.string.checklist_create)) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onChecklistCreate) {
                Icon(
                    imageVector = Icons.Default.Create,
                    contentDescription = stringResource(R.string.desc_create_checklist)
                )
            }
        }
    )
}

@Composable
private fun Header(
    title: String,
    onTitleChange: (String) -> Unit
) {
    TextField(
        value = title,
        onValueChange = { onTitleChange(it) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = TextStyle(fontSize = 18.sp),
        placeholder = { Text("Title") },
        colors = OutlinedTextFieldDefaults.colors()
    )
}
