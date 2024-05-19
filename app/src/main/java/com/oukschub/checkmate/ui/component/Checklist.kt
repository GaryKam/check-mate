package com.oukschub.checkmate.ui.component

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.ChecklistItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.ReorderableLazyListState
import org.burnoutcrew.reorderable.detectReorder
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

/**
 * A container that displays information associated to a checklist.
 */
@Composable
fun Checklist(
    header: @Composable () -> Unit,
    items: ImmutableList<ChecklistItem>,
    onItemCheck: (Int, Boolean) -> Unit,
    onItemNameFocus: (String) -> Unit,
    onItemNameChange: (Int, String) -> Unit,
    onItemNameSet: (Int, String) -> Unit,
    onItemAdd: (String) -> Unit,
    onItemDelete: (Int) -> Unit,
    onItemMove: (Int, Int) -> Unit,
    onItemMoveDone: () -> Unit,
    onDividerCheck: (Int, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false
) {
    val focusManager = LocalFocusManager.current

    ElevatedCard(
        modifier = modifier
            .animateContentSize()
            .fillMaxWidth()
            .padding(10.dp)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        header()
        Checkboxes(
            items = items,
            isEditing = isEditing,
            onItemCheck = onItemCheck,
            onItemNameFocus = onItemNameFocus,
            onItemNameChange = onItemNameChange,
            onItemNameSet = onItemNameSet,
            onItemDelete = onItemDelete,
            onItemMove = onItemMove,
            onDividerCheck = onDividerCheck,
            onItemMoveDone = onItemMoveDone
        )
        InputField(onItemAdd = onItemAdd)
    }
}

@Composable
private fun Checkboxes(
    items: ImmutableList<ChecklistItem>,
    isEditing: Boolean,
    onItemCheck: (Int, Boolean) -> Unit,
    onItemNameFocus: (String) -> Unit,
    onItemNameChange: (Int, String) -> Unit,
    onItemNameSet: (Int, String) -> Unit,
    onItemDelete: (Int) -> Unit,
    onItemMove: (Int, Int) -> Unit,
    onDividerCheck: (Int, Boolean) -> Unit,
    onItemMoveDone: () -> Unit
) {
    val state = rememberReorderableLazyListState(
        onMove = { from, to -> onItemMove(from.index, to.index) },
        onDragEnd = { _, _ -> onItemMoveDone() }
    )
    LazyColumn(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .animateContentSize()
            .fillMaxWidth()
            .height(items.size * 44.dp)
            .padding(horizontal = 20.dp)
            .reorderable(state),
        state = state.listState
    ) {
        itemsIndexed(items = items) { itemIndex, item ->
            ReorderableItem(state = state, index = itemIndex, key = item) { isDragging ->
                val elevation = animateDpAsState(if (isDragging) 3.dp else 0.dp, label = "ElevationAnimation")

                if (item.isDivider) {
                    ChecklistDivider(
                        divider = item,
                        state = state,
                        onDividerNameChange = { onItemNameChange(itemIndex, it) },
                        onDividerNameFocus = { onItemNameFocus(it) },
                        onDividerSet = { onItemNameSet(itemIndex, it) },
                        onDividerCheck = { onDividerCheck(itemIndex, it) },
                        onDividerDelete = { onItemDelete(itemIndex) },
                        modifier = Modifier.shadow(elevation.value),
                        isEditing = isEditing
                    )
                } else {
                    Row(
                        modifier = Modifier
                            .shadow(elevation.value)
                            .fillMaxWidth()
                            .height(44.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item.isChecked,
                            onCheckedChange = { onItemCheck(itemIndex, it) }
                        )

                        BasicTextField(
                            value = item.name,
                            onValueChange = { onItemNameChange(itemIndex, it) },
                            modifier = Modifier
                                .weight(1.0F)
                                .onFocusChanged { focusState ->
                                    if (focusState.isFocused) {
                                        onItemNameFocus(item.name)
                                    } else {
                                        onItemNameSet(itemIndex, item.name)
                                    }
                                },
                            enabled = !item.isChecked,
                            textStyle = TextStyle(
                                textDecoration = if (item.isChecked) {
                                    TextDecoration.LineThrough
                                } else {
                                    TextDecoration.None
                                }
                            ),
                            singleLine = true
                        )

                        if (isEditing) {
                            Icon(
                                painter = painterResource(R.drawable.ic_reorder),
                                contentDescription = null,
                                modifier = Modifier
                                    .detectReorder(state)
                                    .scale(0.5F)
                            )

                            IconButton(onClick = { onItemDelete(itemIndex) }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.desc_delete_checklist_item)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChecklistDivider(
    divider: ChecklistItem,
    state: ReorderableLazyListState,
    onDividerNameChange: (String) -> Unit,
    onDividerNameFocus: (String) -> Unit,
    onDividerSet: (String) -> Unit,
    onDividerCheck: (Boolean) -> Unit,
    onDividerDelete: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = false
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(44.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1.0F)) {
            BasicTextField(
                value = divider.name,
                onValueChange = { onDividerNameChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp)
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            onDividerNameFocus(divider.name)
                        } else {
                            onDividerSet(divider.name)
                        }
                    },
                singleLine = true
            )

            Divider(color = MaterialTheme.colorScheme.secondary)
        }

        if (isEditing) {
            Icon(
                painter = painterResource(R.drawable.ic_reorder),
                contentDescription = null,
                modifier = Modifier
                    .detectReorder(state)
                    .scale(0.5F)
            )

            IconButton(onClick = { onDividerDelete() }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.desc_delete_checklist_item)
                )
            }
        }

        Checkbox(
            checked = divider.isChecked,
            onCheckedChange = { onDividerCheck(it) }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun InputField(onItemAdd: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    val couroutineScope = rememberCoroutineScope()
    val viewRequester = remember { BringIntoViewRequester() }

    TextField(
        value = text,
        onValueChange = { text = it },
        modifier = Modifier
            .fillMaxWidth()
            .bringIntoViewRequester(viewRequester),
        placeholder = { Text(stringResource(R.string.checklist_type_placeholder)) },
        trailingIcon = {
            IconButton(
                onClick = {
                    onItemAdd(text)
                    text = ""
                    couroutineScope.launch {
                        delay(300L)
                        viewRequester.bringIntoView()
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = stringResource(R.string.desc_done)
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = {
                onItemAdd(text)
                text = ""
                couroutineScope.launch {
                    delay(300L)
                    viewRequester.bringIntoView()
                }
            }
        ),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}
