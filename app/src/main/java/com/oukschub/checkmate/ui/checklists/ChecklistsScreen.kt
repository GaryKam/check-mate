package com.oukschub.checkmate.ui.checklists

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
        var isExpanded by remember { mutableStateOf(false) }
        val backdropOffset by animateDpAsState(if (isExpanded) 0.dp else (-60).dp, label = "BackdropAnimation")

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            SearchBar(
                query = viewModel.query,
                onQueryChange = { query -> viewModel.query = query }
            )

            IconButton(onClick = { isExpanded = !isExpanded }) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "todo")
            }
        }

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
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
            ) {
                if (viewModel.checklists.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Logo(isSad = true)
                        Text(stringResource(R.string.checklists_none_found))
                    }
                } else {
                    Content(
                        checklists = viewModel.checklists,
                        onChecklistFavorite = { checklistIndex -> viewModel.favoriteChecklist(checklistIndex) },
                        onChecklistClick = { checklistIndex -> onChecklistClick(checklistIndex) }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit
) {
    androidx.compose.material3.SearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = Modifier.padding(10.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(R.string.checklists_search)
            )
        }
    ) {}
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
                label = { Text(stringResource(status.first)) }
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
                    .padding(10.dp)
                    .clickable { onChecklistClick(checklistIndex) },
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
