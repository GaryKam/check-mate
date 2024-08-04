package com.oukschub.checkmate.ui.addchecklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.R
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddChecklistViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    var title by mutableStateOf("")
    var isAddingChecklist by mutableStateOf(false)
        private set
    private val _items = mutableStateListOf<ChecklistItem>()
    val items: ImmutableList<ChecklistItem>
        get() = ImmutableList.copyOf(_items)
    var isCreatingNewChecklist by mutableStateOf(true)
        private set
    var sharedChecklistCode by mutableStateOf("")
        private set

    fun setCreationType(type: Int) {
        when (type) {
            0 -> isCreatingNewChecklist = true
            1 -> isCreatingNewChecklist = false
        }
    }

    fun setItemChecked(
        itemIndex: Int,
        isChecked: Boolean
    ) {
        _items[itemIndex] = _items[itemIndex].copy(isChecked = isChecked)
    }

    fun setItemName(
        itemIndex: Int,
        itemName: String
    ) {
        _items[itemIndex] = _items[itemIndex].copy(name = itemName)
    }

    fun addItem(itemName: String) {
        if (itemName.isNotBlank()) {
            _items.add(ChecklistItem(itemName, false))
        }
    }

    fun setShareCode(shareCode: String) {
        if (shareCode.matches(Regex("^[A-Z0-9]{0,6}\$"))) {
            sharedChecklistCode = shareCode
        }
    }

    fun addChecklist(onSuccess: (Int) -> Unit) {
        if (isAddingChecklist) {
            return
        }

        isAddingChecklist = true

        viewModelScope.launch {
            if (repository.createChecklist(title, _items)) {
                onSuccess(R.string.add_checklist_create_success)
            }

            isAddingChecklist = false
        }
    }

    fun addSharedChecklist(
        onSuccess: (Int) -> Unit,
        onFailure: (Int) -> Unit
    ) {
        isAddingChecklist = true

        viewModelScope.launch {
            if (repository.createChecklist(sharedChecklistCode)) {
                onSuccess(R.string.add_checklist_shared_success)
            } else {
                onFailure(R.string.add_checklist_shared_fail)
            }

            isAddingChecklist = false
        }
    }

    fun deleteItem(itemIndex: Int) {
        _items.removeAt(itemIndex)
    }
}
