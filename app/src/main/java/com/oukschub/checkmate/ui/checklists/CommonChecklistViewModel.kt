package com.oukschub.checkmate.ui.checklists

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository

open class CommonChecklistViewModel(
    private val repository: ChecklistRepository
) : ViewModel() {
    private var initialTitle: String? = null
    private var initialItemName: String? = null

    fun focusTitle(
        checklistIndex: Int,
        title: String
    ) {
        repository.currentChecklist = checklistIndex
        initialTitle = title
    }

    fun focusItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.currentChecklist = checklistIndex
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

    fun setTitle(
        checklistIndex: Int,
        title: String
    ) {
        if (initialTitle != null && initialTitle != title) {
            repository.updateChecklistTitle(checklistIndex, title)
            initialTitle = null
        }
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

    fun unfavoriteChecklist(checklistIndex: Int) {
        repository.updateChecklistFavorite(checklistIndex, false)
    }

    fun moveItem(
        checklistIndex: Int,
        fromIndex: Int,
        toIndex: Int
    ) {
        initialItemName = null
        repository.moveChecklistItem(checklistIndex, fromIndex, toIndex)
    }

    fun finishMovingItem(checklistIndex: Int) {
        repository.updateChecklistItemPositions(checklistIndex)
    }

    fun deleteItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        initialItemName = null
        repository.deleteChecklistItem(checklistIndex, itemIndex)
    }

    open fun deleteChecklist(checklistIndex: Int) {
        initialTitle = null
        initialItemName = null
        repository.deleteChecklist(checklistIndex)
    }
}
