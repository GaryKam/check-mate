package com.oukschub.checkmate.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.Checklist
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
        SearchBar()
        Filters()
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .height(1.dp)
        )
        Content(viewModel.checklists)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBar() {
    androidx.compose.material3.SearchBar(
        query = "",
        onQueryChange = {},
        onSearch = {},
        active = false,
        onActiveChange = {},
        modifier = Modifier.padding(10.dp),
        leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "Search") }
    ) {
        // TODO
    }
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
        FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text(text = "Private") })
        FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text(text = "Shared") })
        FilterChip(selected = false, onClick = { /*TODO*/ }, label = { Text(text = "Favorite") })
    }
}

@Composable
private fun Content(checklists: ImmutableList<Checklist>) {
    Column(modifier = Modifier.padding(10.dp)) {
        for (checklist in checklists) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = checklist.title)

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        imageVector = Icons.Default.FavoriteBorder,
                        contentDescription = "favorite"
                    )
                }
            }
        }
    }
}
