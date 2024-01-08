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
        database.loadChecklistsFromDb {
            _checklists.add(it)
        }
    }

    fun updateChecklistTitle(
        title: String,
        index: Int
    ) {
        _checklists[index] = _checklists[index].copy(title = title)
    }

    fun sendChecklistTitle(
        title: String,
        index: Int,
        onUpdate: (Int) -> Unit
    ) {
        if (title.isNotEmpty()) {
            database.updateChecklistToDb(
                checklist = _checklists[index],
                onSuccess = {
                    _checklists[index] = _checklists[index].copy(title = title)
                    onUpdate(R.string.checklist_update)
                }
            )
        } else {
            onUpdate(R.string.checklist_update_error)
        }
    }
}
