package com.oukschub.checkmate.data.repository

import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import javax.inject.Inject

class ChecklistRepository @Inject constructor(
    private val database: Database
) {
    private val checklists = mutableListOf<Checklist>()
    private var isInitialized = false

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        val checklist = database.createChecklist(title, items, onSuccess)

        checklists.add(checklist)
    }

    suspend fun getChecklists(): List<Checklist> {
        if (!isInitialized) {
            isInitialized = true
            checklists.clear()
            checklists.addAll(database.fetchChecklists())
        }

        return checklists
    }

    fun setChecklistTitle(
        id: String,
        title: String,
        onSuccess: () -> Unit
    ) {
        database.updateChecklistTitle(id, title, onSuccess)
    }

    fun setChecklistItems(
        id: String,
        items: List<ChecklistItem>
    ) {
        database.updateChecklistItems(id, items)
    }

    fun setChecklistFavorite(
        id: String,
        isFavorite: Boolean
    ) {
        database.updateChecklistFavorite(id, isFavorite)
    }

    fun deleteChecklist(
        id: String,
        onSuccess: () -> Unit
    ) {
        database.deleteChecklist(id, onSuccess)
    }
}
