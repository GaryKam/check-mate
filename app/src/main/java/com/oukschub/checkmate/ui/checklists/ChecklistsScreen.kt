package com.oukschub.checkmate.ui.checklists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.ui.component.Logo

/**
 * The screen that displays the title of each existing checklists.
 */
@Composable
fun ChecklistsScreen(
    viewModel: ChecklistsViewModel,
    onChecklistClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary)
            .pointerInput(Unit) { detectTapGestures(onTap = { focusManager.clearFocus() }) },
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var isBackdropExpanded by remember { mutableStateOf(false) }
        val backdropOffset by animateDpAsState(if (isBackdropExpanded) 0.dp else (-60).dp, label = "BackdropAnimation")

        Backdrop(
            query = viewModel.query,
            onQueryChange = { viewModel.query = it },
            onExpand = { isBackdropExpanded = !isBackdropExpanded }
        )

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(space = backdropOffset, alignment = Alignment.Top)
        ) {
            ChipFilters(
                filters = viewModel.filters,
                onFilterChange = { filterIndex -> viewModel.toggleFilter(filterIndex) }
            )

            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
            ) {
                AnimatedVisibility(visible = viewModel.checklists.isEmpty()) {
                    SadCheckmate()
                }

                Content(
                    checklists = viewModel.checklists,
                    onChecklistFavorite = { checklistIndex -> viewModel.favoriteChecklist(checklistIndex) },
                    onChecklistClick = { checklistIndex -> onChecklistClick(checklistIndex) }
                )
            }
        }
    }
}

@Composable
private fun Backdrop(
    query: String,
    onQueryChange: (String) -> Unit,
    onExpand: () -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HiddenSearchBar(
                query = query,
                onQueryChange = onQueryChange
            )

            IconButton(onClick = onExpand) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = stringResource(R.string.desc_checklists_backdrop),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun HiddenSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }
    val backgroundColor by animateColorAsState(
        if (isExpanded) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.primary
        },
        label = "BackgroundColorAnimation"
    )
    val iconColor by animateColorAsState(
        if (isExpanded) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onPrimary
        },
        label = "IconColorAnimation"
    )

    Row(
        modifier = modifier
            .background(backgroundColor, shape = RoundedCornerShape(8.dp))
            .animateContentSize()
            .width(if (isExpanded) 300.dp else 48.dp)
            .height(TextFieldDefaults.MinHeight),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val focusRequester = remember { FocusRequester() }
        val focusManager = LocalFocusManager.current

        IconButton(
            onClick = {
                isExpanded = !isExpanded
                if (isExpanded) {
                    focusRequester.requestFocus()
                } else {
                    focusManager.clearFocus()
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.desc_checklists_search),
                tint = iconColor
            )
        }

        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 8.dp)
                .focusRequester(focusRequester),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChipFilters(
    filters: ImmutableList<Triple<Int, Boolean, (Checklist) -> Boolean>>,
    onFilterChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        for ((index, status) in filters.withIndex()) {
            FilterChip(
                selected = status.second,
                onClick = { onFilterChange(index) },
                label = { Text(stringResource(status.first)) },
                colors = FilterChipDefaults.filterChipColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )
        }
    }
}

@Composable
private fun Content(
    checklists: ImmutableList<Checklist>,
    onChecklistFavorite: (Int) -> Unit,
    onChecklistClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        itemsIndexed(items = checklists) { checklistIndex, checklist ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChecklistClick(checklistIndex) }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = checklist.title,
                    style = MaterialTheme.typography.titleLarge
                )

                IconButton(onClick = { onChecklistFavorite(checklistIndex) }) {
                    Icon(
                        imageVector = if (checklist.isFavorite) {
                            Icons.Default.Favorite
                        } else {
                            Icons.Default.FavoriteBorder
                        },
                        contentDescription = stringResource(R.string.desc_favorite_checklist),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun SadCheckmate(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Logo(isSad = true)
        Text(stringResource(R.string.checklists_none_found))
    }
}
