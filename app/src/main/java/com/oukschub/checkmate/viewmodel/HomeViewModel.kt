package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: List<Checklist> = _checklists

    init {
        getChecklists {}
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

    fun getChecklists(onSuccess: () -> Unit) {
        _checklists.clear()

        repository.getChecklists(
            onSuccess = {
                _checklists.add(it)
                onSuccess()
            }
        )
    }

    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String,
        onComplete: (Int) -> Unit
    ) {
        if (title.isNotEmpty()) {
            repository.setChecklistTitle(
                id = _checklists[checklistIndex].id,
                title = title,
                onSuccess = {
                    _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
                    onComplete(R.string.checklist_update)
                }
            )
        } else {
            onComplete(R.string.checklist_update_error)
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
