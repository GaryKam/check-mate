package com.oukschub.checkmate.data.model

data class Checklist(
    val id: String = "",
    val title: String = "",
    val items: List<ChecklistItem> = emptyList()
)
