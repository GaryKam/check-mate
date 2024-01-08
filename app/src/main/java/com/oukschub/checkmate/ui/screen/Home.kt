package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.util.MessageUtil
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
        val context = LocalContext.current

        for ((index, checklist) in viewModel.checklists.withIndex()) {
            Checklist(
                title = checklist.title,
                itemList = checklist.items,
                onUpdateTitle = { title ->
                    viewModel.updateChecklistTitle(
                        title = title,
                        index = index,
                        onUpdate = { MessageUtil.displayToast(R.string.checklist_updated, context) }
                    )
                },
                onUpdateItem = { _, _, _ -> },
                onAddItem = {}
            )
        }
    }
}
