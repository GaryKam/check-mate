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
    var isContentVisible by mutableStateOf(false)
    val checklists get() = repository.checklists
    private var initialTitle: String? = null
    var isDeleteChecklistItemDialogVisible by mutableStateOf(false)
        private set
    private var deleteItemChecklistIndex: Int = -1
    private var deleteItemIndex: Int = -1
    val itemToBeDeleted: String
        get() = checklists[deleteItemChecklistIndex].items[deleteItemIndex].name

    fun focusChecklistTitle(title: String) {
        initialTitle = title
    }

    fun showDeleteChecklistItemDialog(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        isDeleteChecklistItemDialogVisible = true
        deleteItemChecklistIndex = checklistIndex
        deleteItemIndex = itemIndex
    }

    fun hideDeleteChecklistItemDialog() {
        isDeleteChecklistItemDialogVisible = false
        deleteItemChecklistIndex = -1
        deleteItemIndex = -1
    }

    fun changeChecklistTitle(
        checklistIndex: Int,
        title: String,
    ) {
        repository.changeChecklistTitle(checklistIndex, title)
    }

    fun changeChecklistItem(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String,
        isChecked: Boolean
    ) {
        repository.updateChecklistItem(checklistIndex, itemIndex, itemName, isChecked)
    }

    fun createChecklistItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.createChecklistItem(checklistIndex, itemName)
    }

    fun updateChecklistTitle(
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
