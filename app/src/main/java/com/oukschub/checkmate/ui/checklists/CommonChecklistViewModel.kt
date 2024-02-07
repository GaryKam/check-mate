package com.oukschub.checkmate.ui.checklists

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository

open class CommonChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {
    fun addItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.createChecklistItem(checklistIndex, itemName)
    }

    fun changeItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        repository.changeItemName(checklistIndex, itemIndex, itemName)
    }

    fun setItemChecked(
        checklistIndex: Int,
        itemIndex: Int,
        isChecked: Boolean
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, isChecked)
    }

    fun setItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, itemName)
    }

    fun deleteItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        repository.deleteChecklistItem(checklistIndex, itemIndex)
    }
}
