package com.oukschub.checkmate.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.ui.checklists.CommonChecklistViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : CommonChecklistViewModel(repository) {
    val checklists get() = repository.checklists
    var isContentVisible by mutableStateOf(false)
    private var initialTitle: String? = null

    fun focusTitle(title: String) {
        initialTitle = title
    }

    fun setTitle(
        checklistIndex: Int,
        title: String
    ) {
        if (initialTitle != null && initialTitle != title) {
            repository.updateChecklistTitle(checklistIndex, title)
            initialTitle = null
        }
    }

    fun unfavoriteChecklist(checklistIndex: Int) {
        repository.updateChecklistFavorite(checklistIndex, false)
    }

    fun deleteChecklist(checklistIndex: Int) {
        repository.deleteChecklist(checklistIndex)
    }
}
