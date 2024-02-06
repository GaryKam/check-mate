package com.oukschub.checkmate.ui.createchecklist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateChecklistViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    var title by mutableStateOf("")
    var isCreatingChecklist by mutableStateOf(false)
        private set
    private val _items = mutableStateListOf<ChecklistItem>()
    val items: ImmutableList<ChecklistItem>
        get() = ImmutableList.copyOf(_items)

    fun changeChecklistTitle(text: String) {
        if (text.isNotBlank()) {
            title = text
        }
    }

    fun changeChecklistItem(
        index: Int,
        name: String,
        isChecked: Boolean
    ) {
        _items[index] = _items[index].copy(name = name, isChecked = isChecked)
    }

    fun addChecklistItem(text: String) {
        if (text.isNotBlank()) {
            _items.add(ChecklistItem(text, false))
        }
    }

    fun addChecklist(onSuccess: () -> Unit) {
        isCreatingChecklist = true

        repository.createChecklist(title, _items) {
            onSuccess()
            isCreatingChecklist = false
        }
    }
}
