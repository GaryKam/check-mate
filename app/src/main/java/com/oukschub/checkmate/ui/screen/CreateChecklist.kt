package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.CreateChecklistViewModel

@Composable
fun CreateChecklist(
    modifier: Modifier = Modifier,
    viewModel: CreateChecklistViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checklist(
            title = viewModel.title.value,
            itemList = viewModel.itemList,
            onUpdateTitle = { viewModel.updateTitle(it) },
            onUpdateItem = { index, newName, newIsChecked ->
                viewModel.updateItem(index, newName, newIsChecked)
            },
            onAddItem = { text -> viewModel.addItem(text) }
        )

        Button(
            onClick = { viewModel.createChecklist(viewModel.title.value, viewModel.itemList) }
        ) {
            Text(text = "Create")
        }
    }
}