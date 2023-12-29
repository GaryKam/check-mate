package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.ChecklistItem

class HomeViewModel(
    private val database: Database = Database()
) : ViewModel() {
    /*val itemList = mutableStateListOf<ChecklistItem>()

    fun addItem(text: String) {
        if (text.isNotBlank()) {
            itemList.add(ChecklistItem(text, false))
        }
    }

    fun updateItem(index: Int, newName: String, newIsChecked: Boolean) {
        itemList[index] = itemList[index].copy(name = newName, isChecked = newIsChecked)
    }*/
}