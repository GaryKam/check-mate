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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.CreateChecklistViewModel
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun CreateChecklist(
    onNavigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateChecklistViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopBar(
                onNavigateToHome = onNavigateToHome,
                onCreateChecklist = {
                    viewModel.createChecklistInDb(
                        onSuccess = {
                            homeViewModel.loadChecklistsFromDb(onSuccess = {
                                onNavigateToHome()
                            })
                        }
                    )
                }
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
                onItemCreate = { viewModel.createChecklistItem(it) },
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
    onNavigateToHome: () -> Unit,
    onCreateChecklist: () -> Unit
) {
    TopAppBar(
        title = { Text(text = stringResource(R.string.checklist_create)) },
        navigationIcon = {
            IconButton(onClick = onNavigateToHome) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.desc_back)
                )
            }
        },
        actions = {
            IconButton(onClick = onCreateChecklist) {
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
        placeholder = { Text(text = "Title") },
        colors = OutlinedTextFieldDefaults.colors()
    )
}
