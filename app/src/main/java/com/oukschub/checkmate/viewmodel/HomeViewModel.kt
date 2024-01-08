package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
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
        index: Int,
        onUpdate: () -> Unit
    ) {
        _checklists[index] = _checklists[index].copy(title = title)
        database.updateChecklistToDb(
            checklist = _checklists[index],
            onSuccess = { onUpdate() }
        )
    }
}
