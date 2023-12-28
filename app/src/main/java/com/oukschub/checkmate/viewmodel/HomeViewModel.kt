package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.ChecklistItem

class HomeViewModel : ViewModel() {
    val itemList = mutableStateListOf<ChecklistItem>()

    fun addItem(text: String) {
        if (text.isNotBlank()) {
            itemList.add(ChecklistItem(text, false))
        }
    }

    fun updateItem(index: Int, newName: String, newIsChecked: Boolean) {
        itemList[index] = itemList[index].copy(name = newName, isChecked = newIsChecked)
    }
}