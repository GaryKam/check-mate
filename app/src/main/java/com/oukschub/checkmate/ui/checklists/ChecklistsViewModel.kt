package com.oukschub.checkmate.ui.checklists

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChecklistsViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    private val _checklists get() = repository.checklists
    val checklists: ImmutableList<Checklist>
        get() {
            var checklists = if (query.isBlank()) {
                _checklists.toList()
            } else {
                _checklists.filter { it.title.contains(query, true) }
            }

            for (filter in _filters) {
                if (filter.second) {
                    checklists = checklists.filter { filter.third(it) }
                }
            }

            return ImmutableList.copyOf(checklists)
        }
    var query by mutableStateOf("")
    private val _filters = mutableStateListOf<Triple<Int, Boolean, (Checklist) -> Boolean>>(
        Triple(R.string.checklists_filter_private, false) { it.isPrivate },
        Triple(R.string.checklists_filter_shared, false) { it.isShared },
        Triple(R.string.checklists_filter_favorite, false) { it.isFavorite }
    )
    val filters: ImmutableList<Triple<Int, Boolean, (Checklist) -> Boolean>>
        get() = ImmutableList.copyOf(_filters)
    private var initialItemName: String? = null

    fun toggleFilter(filterIndex: Int) {
        val filter = _filters[filterIndex]
        _filters[filterIndex] = filter.copy(second = !filter.second)
    }

    fun focusItem(itemName: String) {
        initialItemName = itemName
    }

    fun favoriteChecklist(filteredChecklistIndex: Int) {
        val checklist = checklists[filteredChecklistIndex]
        val isFavorite = !checklist.isFavorite
        val checklistIndex = _checklists.indexOfFirst { it.id == checklist.id }

        repository.updateChecklistFavorite(checklistIndex, isFavorite)
    }

    fun addItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.createChecklistItem(checklistIndex, itemName)
    }

    fun changeItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        repository.changeItemName(checklistIndex, itemIndex, itemName)
    }

    fun setItemChecked(
        checklistIndex: Int,
        itemIndex: Int,
        isChecked: Boolean
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, isChecked)
    }

    fun setItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, itemName)
    }

    fun deleteItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        repository.deleteChecklistItem(checklistIndex, itemIndex)
    }
}
