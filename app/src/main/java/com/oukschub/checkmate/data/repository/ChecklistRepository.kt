package com.oukschub.checkmate.data.repository

import android.util.Log
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import javax.inject.Inject

class ChecklistRepository @Inject constructor(
    private val database: Database
) {
    private val checklists = mutableListOf<Checklist>()

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        val checklist = database.createChecklist(title, items, onSuccess)

        checklists.add(checklist)
    }

    fun loadChecklists(onComplete: () -> Unit) {
        Log.d("fiefie", "start loading checklists")
        database.fetchChecklists(
            onSuccess = {
                checklists.add(it)
                Log.d("fiefie", "load a checklist")
            },
            onComplete = onComplete
        )
    }

    fun getChecklists(): ImmutableList<Checklist> {
        Log.d("fiefie", "get all checklists")
        return ImmutableList.copyOf(checklists)
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
