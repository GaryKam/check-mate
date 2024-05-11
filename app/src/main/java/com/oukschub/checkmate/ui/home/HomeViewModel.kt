package com.oukschub.checkmate.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.ui.checklists.CommonChecklistViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : CommonChecklistViewModel(repository) {
    val checklists: ImmutableList<Checklist>
        get() = repository.checklists
    var isContentVisible by mutableStateOf(false)
        private set
    var isSadCheckMateVisible by mutableStateOf(false)
        private set

    init {
        isContentVisible = true

        viewModelScope.launch {
            delay(500L)
            isSadCheckMateVisible = true
        }
    }
}
