package com.oukschub.checkmate.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    val checklists get() = repository.checklists
    var isContentVisible by mutableStateOf(false)
    var isDeleteItemDialogVisible by mutableStateOf(false)
        private set
    val itemToBeDeleted: String
        get() = checklists[deleteItemChecklistIndex].items[deleteItemIndex].name

    private var initialTitle: String? = null
    private var initialItemName: String? = null
    private var deleteItemChecklistIndex: Int = -1
    private var deleteItemIndex: Int = -1

    fun focusTitle(title: String) {
        initialTitle = title
    }

    fun focusItem(itemName: String) {
        initialItemName = itemName
    }

    fun showDeleteItemDialog(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        isDeleteItemDialogVisible = true
        deleteItemChecklistIndex = checklistIndex
        deleteItemIndex = itemIndex
    }

    fun hideDeleteItemDialog() {
        isDeleteItemDialogVisible = false
        deleteItemChecklistIndex = -1
        deleteItemIndex = -1
    }

    fun addItem(
        checklistIndex: Int,
        itemName: String
    ) {
        repository.createChecklistItem(checklistIndex, itemName)
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
        Timber.d("setItemName $itemName")
        repository.updateChecklistItem(checklistIndex, itemIndex, itemName)
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

    fun deleteItem() {
        repository.deleteChecklistItem(deleteItemChecklistIndex, deleteItemIndex)
        hideDeleteItemDialog()
    }

    fun deleteChecklist(checklistIndex: Int) {
        repository.deleteChecklist(checklistIndex)
    }
}
