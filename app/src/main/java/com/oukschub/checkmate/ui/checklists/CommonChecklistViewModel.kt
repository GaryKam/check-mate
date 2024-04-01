package com.oukschub.checkmate.ui.checklists

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository

open class CommonChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {
    private var initialItemName: String? = null

    fun focusItem(itemName: String) {
        initialItemName = itemName
    }

    fun addItem(
        checklistIndex: Int,
        itemName: String,
        isDivider: Boolean = false
    ) {
        repository.createChecklistItem(checklistIndex, itemName, isDivider)
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
        if (initialItemName != null && initialItemName != itemName) {
            initialItemName = null
            repository.updateChecklistItem(checklistIndex, itemIndex, itemName)
        }
    }

    fun setDividerChecked(
        checklistIndex: Int,
        dividerIndex: Int,
        isChecked: Boolean
    ) {
        repository.updateChecklistDivider(checklistIndex, dividerIndex, isChecked)
    }

    fun clearChecklist(checklistIndex: Int) {
        repository.updateChecklistItems(checklistIndex)
    }

    fun deleteItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        initialItemName = null
        repository.deleteChecklistItem(checklistIndex, itemIndex)
    }
}
