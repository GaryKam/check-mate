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
    var isDeletePromptVisible by mutableStateOf(false)
        private set
    var isEditingChecklist by mutableStateOf(false)
        private set
    private var isDeletingChecklist = false
    var shareCode by mutableStateOf("")
        private set

    fun getChecklist(checklistIndex: Int): Checklist? {
        return if (isDeletingChecklist) null else repository.checklists[checklistIndex]
    }

    fun shareChecklist(checklistIndex: Int) {
        val charPool = ('A'..'Z') + ('0'..'9')
        val code = List(6) { charPool.random() }.joinToString("")
        shareCode = code
        repository.updateChecklistShare(checklistIndex, shareCode)
    }

    fun stopSharingChecklist(checklistIndex: Int) {
        shareCode = ""
        repository.updateChecklistShare(checklistIndex, shareCode)
    }

    fun editChecklist() {
        isEditingChecklist = !isEditingChecklist
    }

    fun promptDeleteChecklist() {
        isDeletePromptVisible = true
    }

    fun hideDeleteChecklistDialog() {
        isDeletePromptVisible = false
    }

    override fun deleteChecklist(checklistIndex: Int) {
        isDeletingChecklist = true
        super.deleteChecklist(checklistIndex)
    }
}
