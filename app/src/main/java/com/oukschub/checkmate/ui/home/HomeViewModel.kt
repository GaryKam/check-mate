package com.oukschub.checkmate.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    val checklists get() = repository.checklists
    var isContentVisible by mutableStateOf(false)
    var isRemoveChecklistItemDialogVisible by mutableStateOf(false)
        private set
    val itemToBeDeleted: String
        get() = checklists[deleteItemChecklistIndex].items[deleteItemIndex].name

    private var initialTitle: String? = null
    private var deleteItemChecklistIndex: Int = -1
    private var deleteItemIndex: Int = -1
    private var initialItemName: String? = null

    fun focusChecklistTitle(title: String) {
        initialTitle = title
    }

    fun focusChecklistItem(itemName: String) {
        initialItemName = itemName
    }

    fun showDeleteChecklistItemDialog(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        isRemoveChecklistItemDialogVisible = true
        deleteItemChecklistIndex = checklistIndex
        deleteItemIndex = itemIndex
    }

    fun hideDeleteChecklistItemDialog() {
        isRemoveChecklistItemDialogVisible = false
        deleteItemChecklistIndex = -1
        deleteItemIndex = -1
    }

    fun addChecklistItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.createChecklistItem(checklistIndex, itemName)
    }

    fun setChecklistItemChecked(
        checklistIndex: Int,
        itemIndex: Int,
        isChecked: Boolean
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, isChecked)
    }

    fun setChecklistItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, itemName)
    }

    fun setChecklistTitle(
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

    fun deleteChecklistItem() {
        repository.deleteChecklistItem(deleteItemChecklistIndex, deleteItemIndex)
        hideDeleteChecklistItemDialog()
    }

    fun deleteChecklist(checklistIndex: Int) {
        repository.deleteChecklist(checklistIndex)
    }
}
