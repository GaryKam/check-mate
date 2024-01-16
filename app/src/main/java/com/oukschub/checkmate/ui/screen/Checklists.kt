package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.oukschub.checkmate.viewmodel.ChecklistsViewModel

@Composable
fun Checklists(
    viewModel: ChecklistsViewModel,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        for (checklist in viewModel.checklists) {
            Text(text = checklist.title)
        }
    }
}
