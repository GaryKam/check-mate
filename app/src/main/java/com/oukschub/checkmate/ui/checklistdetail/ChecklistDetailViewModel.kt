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
    private var isDeletingChecklist = false

    fun getChecklist(checklistIndex: Int): Checklist? {
        return if (isDeletingChecklist) null else repository.checklists[checklistIndex]
    }

    override fun deleteChecklist(checklistIndex: Int) {
        isDeletingChecklist = true
        super.deleteChecklist(checklistIndex)
    }
}
