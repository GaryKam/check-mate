package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.ChecklistItem

class CreateChecklistViewModel(
    private val database: Database = Database()
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

    fun createChecklistItem(text: String) {
        if (text.isNotBlank()) {
            _items.add(ChecklistItem(text, false))
        }
    }

    fun createChecklistInDb(onSuccess: () -> Unit) {
        isCreatingChecklist = true

        database.addChecklist(
            title = title,
            items = _items,
            onSuccess = {
                onSuccess()
                isCreatingChecklist = false
            }
        )
    }
}
