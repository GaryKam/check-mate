package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChecklistsViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: ImmutableList<Checklist>
        get() = ImmutableList.copyOf(_checklists)
    private var isInitialized = false

    fun getChecklists() {
        if (!isInitialized) {
            isInitialized = true

            viewModelScope.launch {
                _checklists.addAll(repository.getChecklists())
            }
        }
    }
}
