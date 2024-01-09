package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
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
                items = checklist.items,
                onTitleChange = { title ->
                    viewModel.changeChecklistTitle(
                        title = title,
                        index = index,
                    )
                },
                onTitleSend = { title ->
                    viewModel.sendChecklistTitle(
                        title = title,
                        index = index,
                        onComplete = { MessageUtil.displayToast(context, it) }
                    )
                },
                onItemChange = { _, _, _ -> },
                onItemCreate = {}
            )
        }
    }
}
