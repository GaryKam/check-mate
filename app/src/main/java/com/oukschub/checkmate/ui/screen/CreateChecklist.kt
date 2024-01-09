package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.CreateChecklistViewModel
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun CreateChecklist(
    onCreateChecklist: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CreateChecklistViewModel = viewModel(),
    homeViewModel: HomeViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checklist(
            title = viewModel.title.value,
            items = viewModel.items,
            onTitleChange = { viewModel.changeChecklistTitle(it) },
            onTitleUpdate = {},
            onItemChange = { index, name, isChecked ->
                viewModel.changeChecklistItem(index, name, isChecked)
            },
            onItemCreate = { viewModel.createChecklistItem(it) },
            onChecklistDelete = {}
        )

        Button(
            onClick = {
                viewModel.createChecklistInDb(
                    title = viewModel.title.value,
                    items = viewModel.items,
                    onSuccess = {
                        homeViewModel.loadChecklistsFromDb(onSuccess = {
                            onCreateChecklist()
                        })
                    }
                )
            }
        ) {
            Text(text = "Create")
        }

        if (viewModel.isCreatingChecklist) {
            CircularProgressIndicator()
        }
    }
}
