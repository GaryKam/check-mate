package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.ChecklistItem

class CreateChecklistViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var title = mutableStateOf("")
    private val _itemList = mutableStateListOf<ChecklistItem>()
    val itemList: List<ChecklistItem> = _itemList

    fun changeChecklistTitle(text: String) {
        if (text.isNotBlank()) {
            title.value = text
        }
    }

    fun changeChecklistItem(
        index: Int,
        name: String,
        isChecked: Boolean
    ) {
        _itemList[index] = _itemList[index].copy(name = name, isChecked = isChecked)
    }

    fun createChecklistItem(text: String) {
        if (text.isNotBlank()) {
            _itemList.add(ChecklistItem(text, false))
        }
    }

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>
    ) {
        database.addChecklistToDb(title, items)
    }
}
