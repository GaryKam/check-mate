package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem

class HomeViewModel(
    private val database: Database = Database()
) : ViewModel() {
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: List<Checklist> = _checklists

    init {
        loadChecklistsFromDb(onSuccess = {})
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

    fun loadChecklistsFromDb(onSuccess: () -> Unit) {
        _checklists.clear()

        database.loadChecklists(onSuccess = {
            _checklists.add(it)
            onSuccess()
        })
    }

    fun updateChecklistTitleInDb(
        checklistIndex: Int,
        title: String,
        onComplete: (Int) -> Unit
    ) {
        if (title.isNotEmpty()) {
            database.updateChecklistTitle(
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

    fun updateChecklistItemInDb(checklistIndex: Int) {
        database.updateChecklistItems(
            id = _checklists[checklistIndex].id,
            items = _checklists[checklistIndex].items
        )
    }

    fun updateChecklistFavoriteInDb(checklistIndex: Int) {
        database.updateChecklistFavorite(
            id = _checklists[checklistIndex].id,
            isFavorite = true
        )
    }

    fun deleteChecklistFromDb(checklist: Checklist) {
        database.deleteChecklist(
            id = checklist.id,
            onSuccess = { _checklists.remove(checklist) }
        )
    }
}
