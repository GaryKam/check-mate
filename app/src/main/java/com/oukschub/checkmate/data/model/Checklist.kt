package com.oukschub.checkmate.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

/**
 * Consists of a title and its associated [ChecklistItem]s.
 */
@IgnoreExtraProperties
data class Checklist(
    val id: String = "",
    val title: String = "",
    val items: List<ChecklistItem> = emptyList(),
    @get:Exclude val isPrivate: Boolean = false,
    @get:Exclude val isShared: Boolean = false,
    @get:Exclude val isFavorite: Boolean = false
)
