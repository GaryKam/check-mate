package com.oukschub.checkmate.ui.checklists

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.ui.component.Checklist

/**
 * The screen that displays all existing checklists.
 */
@Composable
fun ChecklistsScreen(
    viewModel: ChecklistsViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SearchBar(
            query = viewModel.query,
            onQueryChange = { query -> viewModel.query = query }
        )
        ChipFilters(
            filters = viewModel.filters,
            onFilterChange = { filterIndex -> viewModel.toggleFilter(filterIndex) }
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(1.dp)
        )
        Content(
            checklists = viewModel.checklists,
            onChecklistFavorite = { checklistIndex ->
                viewModel.favoriteChecklist(checklistIndex)
            },
            onItemCheck = { checklistIndex, itemIndex, isChecked ->
                viewModel.setItemChecked(checklistIndex, itemIndex, isChecked)
            },
            onItemNameFocus = { itemName ->
                viewModel.focusItem(itemName)
            },
            onItemNameChange = { checklistIndex, itemIndex, itemName ->
                viewModel.changeItemName(checklistIndex, itemIndex, itemName)
            },
            onItemNameSet = { checklistIndex, itemIndex, itemName ->
                viewModel.setItemName(checklistIndex, itemIndex, itemName)
            },
            onItemAdd = { checklistIndex, itemName ->
                viewModel.addItem(checklistIndex, itemName)
            },
            onItemDelete = { checklistIndex, itemIndex ->
                viewModel.deleteItem(checklistIndex, itemIndex)
            }
        )
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
    onItemCheck: (Int, Int, Boolean) -> Unit,
    onItemNameFocus: (String) -> Unit,
    onItemNameChange: (Int, Int, String) -> Unit,
    onItemNameSet: (Int, Int, String) -> Unit,
    onItemAdd: (Int, String) -> Unit,
    onItemDelete: (Int, Int) -> Unit
) {
    LazyColumn {
        itemsIndexed(items = checklists) { checklistIndex, checklist ->
            var isExpanded by remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded }
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = checklist.title,
                        style = MaterialTheme.typography.titleLarge
                    )

                    IconButton(
                        onClick = { onChecklistFavorite(checklistIndex) }
                    ) {
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

                AnimatedVisibility(visible = isExpanded) {
                    Checklist(
                        header = {},
                        items = ImmutableList.copyOf(checklist.items),
                        onItemCheck = { itemIndex, isChecked ->
                            onItemCheck(checklistIndex, itemIndex, isChecked)
                        },
                        onItemNameFocus = { itemName ->
                            onItemNameFocus(itemName)
                        },
                        onItemNameChange = { itemIndex, itemName ->
                            onItemNameChange(checklistIndex, itemIndex, itemName)
                        },
                        onItemNameSet = { itemIndex, itemName ->
                            onItemNameSet(checklistIndex, itemIndex, itemName)
                        },
                        onItemAdd = { itemName ->
                            onItemAdd(checklistIndex, itemName)
                        },
                        onItemDelete = { itemIndex ->
                            onItemDelete(checklistIndex, itemIndex)
                        }
                    )
                }
            }
        }
    }
}
