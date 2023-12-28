package com.oukschub.checkmate.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.ChecklistItem

class HomeViewModel : ViewModel() {
    val itemList = mutableStateListOf<ChecklistItem>()

    fun updateItem(index: Int, newIsChecked: Boolean){
        itemList[index] = itemList[index].copy(
            isChecked = newIsChecked
        )
    }
}