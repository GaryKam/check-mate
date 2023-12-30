package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist

class HomeViewModel(
    database: Database = Database()
) : ViewModel() {
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: List<Checklist> = _checklists

    init {
        database.loadChecklists {
            _checklists.add(it)
        }
    }
}
