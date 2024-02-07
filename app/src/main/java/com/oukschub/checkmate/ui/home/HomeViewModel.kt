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
    private var initialTitle: String? = null
    private var initialItemName: String? = null

    fun focusTitle(title: String) {
        initialTitle = title
    }

    fun focusItem(itemName: String) {
        initialItemName = itemName
    }

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

    fun deleteItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        repository.deleteChecklistItem(checklistIndex, itemIndex)
    }

    fun deleteChecklist(checklistIndex: Int) {
        repository.deleteChecklist(checklistIndex)
    }
}
