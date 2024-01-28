package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChecklistsViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    private val _checklists = repository.checklists
    val checklists: ImmutableList<Checklist>
        get() {
            return if (query.isBlank()) {
                ImmutableList.copyOf(_checklists)
            } else {
                ImmutableList.copyOf(_checklists.filter { it.title.contains(query, true) })
            }
        }
    var query by mutableStateOf("")
        private set

    fun changeQuery(query: String) {
        this.query = query
    }
}
