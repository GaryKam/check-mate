package com.oukschub.checkmate.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.oukschub.checkmate.data.ChecklistItem
import com.oukschub.checkmate.viewmodel.HomeViewModel

@Composable
fun Home(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = "Home")

        var text by remember {
            mutableStateOf("")
        }

/*        viewModel.itemsList.forEach { item ->
            TextField(
                value = item.name,
                leadingIcon = {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = {item.isChecked = it}
                        )
                },
                onValueChange = {value ->
                    item.name = value
                }
            )
        }*/

        if(viewModel.itemList.isNotEmpty()) {
            for ((index, checklistItem) in viewModel.itemList.withIndex()) {
                Row {
                    Text(text = checklistItem.name)
                    Checkbox(
                        checked = checklistItem.isChecked,
                        onCheckedChange = {
                            viewModel.updateItem(index)
                        }
                    )
                }
            }
        }

        TextField(
            value = text,
            onValueChange = { text = it },
            placeholder = { Text(text = "Type here") },
            trailingIcon = {
                IconButton(
                    onClick = {
                        viewModel.itemList.add(ChecklistItem(text, false))
                        text = ""
                    }
                ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = null)
            } }
        )
    }
}