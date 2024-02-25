package com.oukschub.checkmate.ui.checklistdetail

import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.ui.checklists.CommonChecklistViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChecklistDetailViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : CommonChecklistViewModel(repository) {
    fun getChecklist(checklistIndex: Int): Checklist {
        return repository.checklists[checklistIndex]
    }

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
