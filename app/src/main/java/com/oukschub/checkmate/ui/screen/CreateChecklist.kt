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
import androidx.compose.ui.res.stringResource
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
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checklist(
            title = viewModel.title,
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
                    onSuccess = {
                        homeViewModel.loadChecklistsFromDb(onSuccess = {
                            onNavigateToHome()
                        })
                    }
                )
            }
        ) {
            Text(text = stringResource(R.string.checklist_create))
        }

        if (viewModel.isCreatingChecklist) {
            CircularProgressIndicator()
        }
    }
}
