package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.google.common.collect.ImmutableList
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
    private val _checklists = mutableStateListOf<Checklist>()
    val checklists: ImmutableList<Checklist>
        get() = ImmutableList.copyOf(_checklists)

    fun changeItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        val items = _checklists[checklistIndex].items.toMutableList()
        items[itemIndex] = items[itemIndex].copy(name = itemName)
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
    }

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        database.createChecklist(title, items, onSuccess).also { checklist ->
            _checklists.add(checklist)
        }
    }

    fun createChecklistItem(
        checklistIndex: Int,
        itemName: String
    ) {
        val item = ChecklistItem(itemName)
        _checklists[checklistIndex].items.toMutableList().apply {
            add(item)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.createChecklistItem(_checklists[checklistIndex].id, item)
        }
    }

    suspend fun fetchChecklists() {
        Timber.d("Fetching checklists from database")
        _checklists.clear()
        _checklists.addAll(database.readChecklists())
    }

    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
        database.updateChecklistTitle(_checklists[checklistIndex].id, title)
    }

    fun updateChecklistItem(
        checklistIndex: Int,
        itemIndex: Int,
        isChecked: Boolean
    ) {
        _checklists[checklistIndex].items.toMutableList().apply {
            this[itemIndex] = this[itemIndex].copy(isChecked = isChecked)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.updateChecklistItems(_checklists[checklistIndex].id, items)
        }
    }

    fun updateChecklistItem(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        _checklists[checklistIndex].items.toMutableList().apply {
            this[itemIndex] = this[itemIndex].copy(name = itemName)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.updateChecklistItems(_checklists[checklistIndex].id, items)
        }
    }

    fun updateChecklistDivider(
        checklistIndex: Int,
        dividerIndex: Int,
        isChecked: Boolean
    ) {
        val myChecklistItems = _checklists[checklistIndex].items.toMutableList()
        myChecklistItems[dividerIndex] = myChecklistItems[dividerIndex].copy(isChecked = isChecked)
        for (i in dividerIndex + 1..<myChecklistItems.size) {
            if (myChecklistItems[i].isDivider) {
                break
            } else {
                myChecklistItems[i] = myChecklistItems[i].copy(isChecked = isChecked)
            }
        }
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = myChecklistItems)
        database.updateChecklistItems(_checklists[checklistIndex].id, myChecklistItems)
    }

    fun updateChecklistFavorite(
        checklistIndex: Int,
        isFavorite: Boolean
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(isFavorite = isFavorite)
        database.updateChecklistFavorite(_checklists[checklistIndex].id, isFavorite)
    }

    fun deleteChecklist(checklistIndex: Int) {
        _checklists[checklistIndex].run {
            _checklists.remove(this)
            database.deleteChecklist(id)
        }
    }

    fun deleteChecklistItem(
        checklistIndex: Int,
        itemIndex: Int
    ) {
        val item = _checklists[checklistIndex].items[itemIndex]
        _checklists[checklistIndex].items.toMutableList().apply {
            remove(item)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.deleteChecklistItem(_checklists[checklistIndex].id, item)
        }
    }
}
