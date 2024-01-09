package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist

class HomeViewModel(
    private val database: Database = Database()
) : ViewModel() {
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: List<Checklist> = _checklists

    init {
        loadChecklistsFromDb(onSuccess = {})
    }

    fun changeChecklistTitle(
        title: String,
        index: Int
    ) {
        _checklists[index] = _checklists[index].copy(title = title)
    }

    fun loadChecklistsFromDb(onSuccess: () -> Unit) {
        _checklists.clear()

        database.loadChecklists(onSuccess = {
            _checklists.add(it)
            onSuccess()
        })
    }

    fun updateChecklistTitleInDb(
        title: String,
        index: Int,
        onComplete: (Int) -> Unit
    ) {
        if (title.isNotEmpty()) {
            database.updateChecklist(
                checklist = _checklists[index],
                onSuccess = {
                    _checklists[index] = _checklists[index].copy(title = title)
                    onComplete(R.string.checklist_update)
                }
            )
        } else {
            onComplete(R.string.checklist_update_error)
        }
    }

    fun updateChecklistItemInDb(
        name: String,
        onComplete: (Int) -> Unit
    ) {
        if (name.isNotEmpty()) {
            onComplete(R.string.checklist_update)
        } else {
            onComplete(R.string.checklist_update_error)
        }
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
