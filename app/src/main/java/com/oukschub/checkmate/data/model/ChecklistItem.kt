package com.oukschub.checkmate.data.model

import com.google.firebase.firestore.PropertyName

/**
 * An item inside a [Checklist].
 */
data class ChecklistItem(
    val name: String = "",
    @get:PropertyName("isChecked")
    val isChecked: Boolean = false,
    @get:PropertyName("isDivider")
    val isDivider: Boolean = false
)
