package com.oukschub.checkmate.ui.screen

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
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.oukschub.checkmate.viewmodel.ChecklistsViewModel

@Composable
fun Checklists(
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
            onQueryChange = { viewModel.changeQuery(it) }
        )
        Filters()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(1.dp)
        )
        Content(checklists = viewModel.checklists)
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
private fun Filters() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        FilterChip(
            selected = false,
            onClick = { /*TODO*/ },
            label = { Text(stringResource(R.string.checklists_filter_private)) }
        )

        FilterChip(
            selected = false,
            onClick = { /*TODO*/ },
            label = { Text(stringResource(R.string.checklists_filter_shared)) }
        )

        FilterChip(
            selected = false,
            onClick = { /*TODO*/ },
            label = { Text(stringResource(R.string.checklists_filter_favorite)) }
        )
    }
}

@Composable
private fun Content(checklists: ImmutableList<Checklist>) {
    LazyColumn {
        items(items = checklists) { checklist ->
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

                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.FavoriteBorder,
                            contentDescription = "favorite"
                        )
                    }
                }

                AnimatedVisibility(visible = isExpanded) {
                    Checklist(
                        header = { /*TODO*/ },
                        items = ImmutableList.copyOf(checklist.items),
                        onItemChange = { _, _, _ -> },
                        onItemCreate = {}
                    )
                }
            }
        }
    }
}
