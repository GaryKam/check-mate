package com.oukschub.checkmate.data.model

data class Checklist(
    val title: String = "",
    val items: List<ChecklistItem> = emptyList()
)
