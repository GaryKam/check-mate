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
    var initialTitle = ""

    init {
        loadChecklistsFromDb(onSuccess = {})
    }

    fun onFocusChecklistTitle(title: String) {
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

        _checklists[checklistIndex] = _checklists[checklistIndex]
            .copy(items = items)
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
        if (initialTitle != title) {
            database.updateChecklistTitle(
                id = _checklists[checklistIndex].id,
                title = title,
                onSuccess = {
                    _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
                    onComplete(R.string.checklist_update)
                }
            )
        }
    }

    fun updateChecklistItemInDb(checklistIndex: Int) {
        database.updateChecklistItems(
            id = _checklists[checklistIndex].id,
            items = _checklists[checklistIndex].items
        )
    }

    fun deleteChecklistFromDb(checklist: Checklist) {
        database.deleteChecklist(
            id = checklist.id,
            onSuccess = {
                _checklists.remove(checklist)
            }
        )
    }
}
