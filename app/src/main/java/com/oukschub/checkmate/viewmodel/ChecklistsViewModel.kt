package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
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
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: ImmutableList<Checklist>
        get() = ImmutableList.copyOf(_checklists)

    fun getChecklists() {
        _checklists.clear()

        repository.getChecklists {
            _checklists.add(it)
        }
    }
}
