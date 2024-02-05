package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import timber.log.Timber
import javax.inject.Inject

/**
 * The single source for checklists.
 */
class ChecklistRepository @Inject constructor(
    private val database: Database
) {
    val checklists = mutableStateListOf<Checklist>()

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        database.createChecklist(title, items, onSuccess).also {
            checklists.add(it)
        }
    }

    fun createChecklistItem(
        id: String,
        name: String
    ) {
        database.createChecklistItem(id, ChecklistItem(name))
    }

    suspend fun getChecklists() {
        Timber.d("Fetching checklists from database")
        checklists.clear()
        checklists.addAll(database.fetchChecklists())
    }

    fun updateChecklistTitle(
        id: String,
        title: String,
        onSuccess: () -> Unit
    ) {
        database.updateChecklistTitle(id, title, onSuccess)
    }

    fun updateChecklistItems(
        id: String,
        items: List<ChecklistItem>
    ) {
        database.updateChecklistItems(id, items)
    }

    fun updateChecklistFavorite(
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

    fun deleteChecklistItem(
        id: String,
        item: ChecklistItem
    ) {
        database.deleteChecklistItem(id, item)
    }
}
