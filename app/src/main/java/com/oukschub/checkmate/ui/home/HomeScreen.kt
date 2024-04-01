package com.oukschub.checkmate.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.ui.component.Checklist
import com.oukschub.checkmate.ui.component.Logo

/**
 * The screen displayed after Splash screen completes.
 * Displays the user's favorite checklists.
 */
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        viewModel.isContentVisible = true
    }

    DisposableEffect(key1 = viewModel) {
        onDispose { viewModel.onStop() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
    ) {
        Card(
            modifier = Modifier
                .padding(top = 10.dp)
                .fillMaxSize(),
            shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            if (viewModel.checklists.none { it.isFavorite }) {
                SadCheckmate(modifier = modifier)
            } else {
                val dividerText = stringResource(R.string.checklist_default_divider)
                Content(
                    checklists = viewModel.checklists,
                    isContentVisible = viewModel.isContentVisible,
                    onTitleFocus = { checklistIndex, title -> viewModel.focusTitle(checklistIndex, title) },
                    onTitleSet = { checklistIndex, title -> viewModel.setTitle(checklistIndex, title) },
                    onChecklistUnfavorite = { checklistIndex -> viewModel.unfavoriteChecklist(checklistIndex) },
                    onChecklistDelete = { checklistIndex -> viewModel.deleteChecklist(checklistIndex) },
                    onChecklistClear = { checklistIndex -> viewModel.clearChecklist(checklistIndex) },
                    onItemCheck = { checklistIndex, itemIndex, isChecked ->
                        viewModel.setItemChecked(
                            checklistIndex,
                            itemIndex,
                            isChecked
                        )
                    },
                    onItemNameFocus = { checklistIndex, itemName -> viewModel.focusItem(checklistIndex, itemName) },
                    onItemNameChange = { checklistIndex, itemIndex, itemName ->
                        viewModel.changeItemName(
                            checklistIndex,
                            itemIndex,
                            itemName
                        )
                    },
                    onItemNameSet = { checklistIndex, itemIndex, itemName ->
                        viewModel.setItemName(
                            checklistIndex,
                            itemIndex,
                            itemName
                        )
                    },
                    onItemAdd = { checklistIndex, itemName -> viewModel.addItem(checklistIndex, itemName) },
                    onItemDelete = { checklistIndex, itemIndex -> viewModel.deleteItem(checklistIndex, itemIndex) },
                    onDividerCheck = { checklistIndex, dividerIndex, isChecked ->
                        viewModel.setDividerChecked(
                            checklistIndex,
                            dividerIndex,
                            isChecked
                        )
                    },
                    onDividerAdd = { checklistIndex -> viewModel.addItem(checklistIndex, dividerText, true) },
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun Content(
    checklists: ImmutableList<Checklist>,
    isContentVisible: Boolean,
    onTitleFocus: (Int, String) -> Unit,
    onTitleSet: (Int, String) -> Unit,
    onChecklistUnfavorite: (Int) -> Unit,
    onChecklistDelete: (Int) -> Unit,
    onChecklistClear: (Int) -> Unit,
    onItemCheck: (Int, Int, Boolean) -> Unit,
    onItemNameFocus: (Int, String) -> Unit,
    onItemNameChange: (Int, Int, String) -> Unit,
    onItemNameSet: (Int, Int, String) -> Unit,
    onItemAdd: (Int, String) -> Unit,
    onItemDelete: (Int, Int) -> Unit,
    onDividerCheck: (Int, Int, Boolean) -> Unit,
    onDividerAdd: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) }
            .graphicsLayer(compositingStrategy = CompositingStrategy.Offscreen)
            .drawWithContent {
                drawContent()
                drawRect(
                    brush = Brush.verticalGradient(
                        0.0F to Color.Transparent, 0.04F to Color.Red,
                        0.96F to Color.Red, 1.0F to Color.Transparent
                    ),
                    blendMode = BlendMode.DstIn
                )
            },
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        itemsIndexed(
            items = checklists.filter { it.isFavorite },
            key = { _, checklist -> checklist.id }
        ) { checklistIndex, checklist ->
            AnimatedVisibility(
                visible = isContentVisible,
                enter = fadeIn(tween(200, 80 * checklistIndex)) +
                    slideInVertically(tween(200, 80 * checklistIndex), initialOffsetY = { it / 2 })
            ) {
                Checklist(
                    header = {
                        Header(
                            title = checklist.title,
                            onTitleFocus = { title -> onTitleFocus(checklistIndex, title) },
                            onTitleSet = { title -> onTitleSet(checklistIndex, title) },
                            onChecklistUnfavorite = { onChecklistUnfavorite(checklistIndex) },
                            onChecklistDelete = { onChecklistDelete(checklistIndex) },
                            onChecklistClear = { onChecklistClear(checklistIndex) },
                            onDividerAdd = { onDividerAdd(checklistIndex) }
                        )
                    },
                    items = ImmutableList.copyOf(checklist.items),
                    onItemCheck = { itemIndex, isChecked -> onItemCheck(checklistIndex, itemIndex, isChecked) },
                    onItemNameFocus = { itemName -> onItemNameFocus(checklistIndex, itemName) },
                    onItemNameChange = { itemIndex, itemName -> onItemNameChange(checklistIndex, itemIndex, itemName) },
                    onItemNameSet = { itemIndex, itemName -> onItemNameSet(checklistIndex, itemIndex, itemName) },
                    onItemAdd = { itemName -> onItemAdd(checklistIndex, itemName) },
                    onItemDelete = { itemIndex -> onItemDelete(checklistIndex, itemIndex) },
                    onDividerCheck = { dividerIndex, isChecked ->
                        onDividerCheck(
                            checklistIndex,
                            dividerIndex,
                            isChecked
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun Header(
    title: String,
    onTitleFocus: (String) -> Unit,
    onTitleSet: (String) -> Unit,
    onChecklistUnfavorite: () -> Unit,
    onChecklistDelete: () -> Unit,
    onChecklistClear: () -> Unit,
    onDividerAdd: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primaryContainer)
            .padding(start = 20.dp, top = 5.dp, end = 5.dp)
    ) {
        var checklistTitle by remember { mutableStateOf(title) }
        TextField(
            value = checklistTitle,
            onValueChange = { checklistTitle = it },
            modifier = Modifier
                .weight(1.0F)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onTitleFocus(checklistTitle)
                    } else {
                        onTitleSet(checklistTitle)
                    }
                },
            textStyle = TextStyle(fontSize = 18.sp),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        Box {
            var isMenuVisible by remember { mutableStateOf(false) }

            IconButton(onClick = { isMenuVisible = true }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(R.string.desc_checklist_options)
                )
            }

            DropdownMenu(
                expanded = isMenuVisible,
                onDismissRequest = { isMenuVisible = false }
            ) {
                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_unfavorite)) },
                    onClick = {
                        isMenuVisible = false
                        onChecklistUnfavorite()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_delete)) },
                    onClick = {
                        isMenuVisible = false
                        onChecklistDelete()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_clear)) },
                    onClick = {
                        isMenuVisible = false
                        onChecklistClear()
                    }
                )

                DropdownMenuItem(
                    text = { Text(stringResource(R.string.checklist_add_divider)) },
                    onClick = {
                        isMenuVisible = false
                        onDividerAdd()
                    }
                )
            }
        }
    }
}

@Composable
private fun SadCheckmate(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(isSad = true)
        Text(stringResource(R.string.home_no_favorites))
    }
}
