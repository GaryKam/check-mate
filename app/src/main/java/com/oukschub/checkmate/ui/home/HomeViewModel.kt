package com.oukschub.checkmate.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    var isContentVisible by mutableStateOf(false)
    private val _checklists = repository.checklists
    val checklists: ImmutableList<Checklist>
        get() = ImmutableList.copyOf(_checklists)
    private var initialTitle: String? = null

    fun focusChecklistTitle(title: String) {
        initialTitle = title
    }

    fun changeChecklistTitle(
        checklistIndex: Int,
        title: String,
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
    }

    fun changeChecklistItem(
        checklistIndex: Int,
        itemIndex: Int,
        name: String,
        isChecked: Boolean
    ) {
        val items = _checklists[checklistIndex].items.toMutableList()
        items[itemIndex] = ChecklistItem(name, isChecked)

        _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
    }

    fun addChecklistItem(
        checklistIndex: Int,
        name: String
    ) {
        val items = _checklists[checklistIndex].items.toMutableList()
        items.add(ChecklistItem(name))

        _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
    }

    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String
    ) {
        if (initialTitle != null && initialTitle != title) {
            repository.updateChecklistTitle(
                id = _checklists[checklistIndex].id,
                title = title,
                onSuccess = {
                    _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
                }
            )

            initialTitle = null
        }
    }

    fun createChecklistItem(
        checklistIndex: Int,
        name: String
    ) {
        repository.createChecklistItem(
            id = _checklists[checklistIndex].id,
            name = name
        )
    }

    fun updateChecklistItems(checklistIndex: Int) {
        repository.updateChecklistItems(
            id = _checklists[checklistIndex].id,
            items = _checklists[checklistIndex].items
        )
    }

    fun updateChecklistFavorite(checklistIndex: Int) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(isFavorite = false)

        repository.updateChecklistFavorite(
            id = _checklists[checklistIndex].id,
            isFavorite = false
        )
    }

    fun deleteChecklist(checklist: Checklist) {
        repository.deleteChecklist(
            id = checklist.id,
            onSuccess = { _checklists.remove(checklist) }
        )
    }
}
