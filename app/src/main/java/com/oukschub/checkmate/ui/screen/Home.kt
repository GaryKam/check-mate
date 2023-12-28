package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.oukschub.checkmate.component.Checklist
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun Home(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Checklist(
            itemList = viewModel.itemList,
            onUpdateItem = { index, newName, newIsChecked ->
                viewModel.updateItem(index, newName, newIsChecked)
            },
            onAddItem = { text -> viewModel.addItem(text) }
        )
    }
}