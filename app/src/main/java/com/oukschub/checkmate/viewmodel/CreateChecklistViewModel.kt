package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem

class CreateChecklistViewModel(
    private val database: Database = Database()
) : ViewModel() {
    var title = mutableStateOf("")
    private val _itemList = mutableStateListOf<ChecklistItem>()
    val itemList: List<ChecklistItem> = _itemList

    fun updateTitle(text: String) {
        if (text.isNotBlank()) {
            title.value = text
        }
    }

    fun updateItem(index: Int, newName: String, newIsChecked: Boolean) {
        _itemList[index] = _itemList[index].copy(name = newName, isChecked = newIsChecked)
    }

    fun addItem(text: String) {
        if (text.isNotBlank()) {
            _itemList.add(ChecklistItem(text, false))
        }
    }

    fun createChecklist(title: String, items: List<ChecklistItem>) {
        val checklist = Checklist(title, items)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        database.createChecklist(checklist, userId)
    }
}