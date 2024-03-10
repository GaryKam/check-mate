package com.oukschub.checkmate.ui.addchecklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.common.collect.ImmutableList
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
    var isCreatingChecklist by mutableStateOf(false)
        private set
    private val _items = mutableStateListOf<ChecklistItem>()
    val items: ImmutableList<ChecklistItem>
        get() = ImmutableList.copyOf(_items)

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

    fun addChecklist(onSuccess: () -> Unit) {
        isCreatingChecklist = true

        viewModelScope.launch {
            if (repository.createChecklist(title, _items)) {
                onSuccess()
            }

            isCreatingChecklist = false
        }
    }

    fun deleteItem(itemIndex: Int) {
        _items.removeAt(itemIndex)
    }
}
