package com.oukschub.checkmate.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.ChecklistItem

@Composable
fun Checklist(
    itemList: SnapshotStateList<ChecklistItem>,
    onUpdateItem: (Int, String, Boolean) -> Unit,
    onAddItem: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 50.dp, end = 10.dp)
    ) {
        Header()
        Checkboxes(itemList = itemList, onUpdateItem = onUpdateItem)
        InputField(onAddItem = onAddItem)
    }
}

@Composable
private fun Header() {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 5.dp, end = 5.dp)
    ) {
        Text(text = "Checklist Title", style = MaterialTheme.typography.headlineSmall)

        IconButton(onClick = { /*TODO*/ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.desc_options)
            )
        }
    }
}

@Composable
private fun Checkboxes(
    itemList: SnapshotStateList<ChecklistItem>,
    onUpdateItem: (Int, String, Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 20.dp)
    ) {
        for ((index, item) in itemList.withIndex()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .background(Color.Transparent)
                    .fillMaxWidth()
            ) {
                Checkbox(
                    checked = item.isChecked,
                    onCheckedChange = { onUpdateItem(index, item.name, it) }
                )

                BasicTextField(
                    value = item.name,
                    onValueChange = { onUpdateItem(index, it, item.isChecked) },
                    enabled = !item.isChecked,
                    textStyle = TextStyle(textDecoration = if (item.isChecked) TextDecoration.LineThrough else TextDecoration.None)
                )
            }
        }
    }
}

@Composable
private fun InputField(
    onAddItem: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = stringResource(R.string.type_placeholder)) },
        trailingIcon = {
            IconButton(onClick = {
                onAddItem(text)
                text = ""
            }) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.desc_done)
                )
            }
        }
    )
}