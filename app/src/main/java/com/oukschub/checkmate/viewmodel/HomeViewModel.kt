package com.oukschub.checkmate.viewmodel

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

    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String
    ) {
        if (initialTitle != null && initialTitle != title) {
            repository.setChecklistTitle(
                id = _checklists[checklistIndex].id,
                title = title,
                onSuccess = {
                    _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
                }
            )

            initialTitle = null
        }
    }

    fun updateChecklistItem(checklistIndex: Int) {
        repository.setChecklistItems(
            id = _checklists[checklistIndex].id,
            items = _checklists[checklistIndex].items
        )
    }

    fun updateChecklistFavorite(checklistIndex: Int) {
        repository.setChecklistFavorite(
            id = _checklists[checklistIndex].id,
            isFavorite = true
        )
    }

    fun deleteChecklist(checklist: Checklist) {
        repository.deleteChecklist(
            id = checklist.id,
            onSuccess = { _checklists.remove(checklist) }
        )
    }
}
