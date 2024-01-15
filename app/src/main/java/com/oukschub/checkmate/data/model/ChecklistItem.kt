package com.oukschub.checkmate.data.model

import com.google.firebase.firestore.PropertyName

data class ChecklistItem(
    val name: String = "",
    @get:PropertyName("isChecked")
    val isChecked: Boolean = false
)
