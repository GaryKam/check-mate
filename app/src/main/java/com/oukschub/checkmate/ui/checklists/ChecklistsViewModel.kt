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
        private set
    private val _filters = mutableStateListOf<Triple<Int, Boolean, (Checklist) -> Boolean>>(
        Triple(R.string.checklists_filter_private, false) { it.isPrivate },
        Triple(R.string.checklists_filter_shared, false) { it.isShared },
        Triple(R.string.checklists_filter_favorite, false) { it.isFavorite }
    )
    val filters: ImmutableList<Triple<Int, Boolean, (Checklist) -> Boolean>>
        get() = ImmutableList.copyOf(_filters)

    fun changeQuery(query: String) {
        this.query = query
    }

    fun changeFilter(filterIndex: Int) {
        val filter = _filters[filterIndex]
        _filters[filterIndex] = filter.copy(second = !filter.second)
    }

    fun favoriteChecklist(filteredChecklistIndex: Int) {
        val checklist = checklists[filteredChecklistIndex]
        val isFavorite = !checklist.isFavorite
        val checklistIndex = _checklists.indexOfFirst { it.id == checklist.id }

        repository.updateChecklistFavorite(checklistIndex, isFavorite)
    }
}
