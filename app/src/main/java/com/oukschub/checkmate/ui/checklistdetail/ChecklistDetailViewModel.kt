package com.oukschub.checkmate.ui.checklistdetail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.ui.checklists.CommonChecklistViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChecklistDetailViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : CommonChecklistViewModel(repository) {
    var isEditingChecklist by mutableStateOf(false)
        private set
    private var isDeletingChecklist = false

    fun getChecklist(checklistIndex: Int): Checklist? {
        return if (isDeletingChecklist) null else repository.checklists[checklistIndex]
    }

    fun editChecklist() {
        isEditingChecklist = !isEditingChecklist
    }

    override fun deleteChecklist(checklistIndex: Int) {
        isDeletingChecklist = true
        super.deleteChecklist(checklistIndex)
    }
}
