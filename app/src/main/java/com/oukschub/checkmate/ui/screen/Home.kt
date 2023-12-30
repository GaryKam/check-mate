package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun Home(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel()
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (checklist in viewModel.checklists) {
            Checklist(
                title = checklist.title,
                itemList = checklist.items,
                onUpdateTitle = {},
                onUpdateItem = { _, _, _ -> },
                onAddItem = {}
            )
        }
    }
}
