package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import timber.log.Timber
import javax.inject.Inject

/**
 * Centralizes changes to data, and interacts with our data source.
 */
class ChecklistRepository @Inject constructor(
    private val database: Database
) {
    /** The single source of checklists. */
    private val _checklists = mutableStateListOf<Checklist>()

    /** A read-only copy for others to use. */
    val checklists: ImmutableList<Checklist>
        get() = ImmutableList.copyOf(_checklists)

    /**
     * Allows user to see latest text value while typing. Modifies local [_checklists].
     */
    fun changeItemName(
        checklistIndex: Int,
        itemIndex: Int,
        itemName: String
    ) {
        val items = _checklists[checklistIndex].items.toMutableList()
        items[itemIndex] = items[itemIndex].copy(name = itemName)
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
    }

    /**
     * Adds a new checklist in [database].
     */
    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        database.createChecklist(title, items, onSuccess).also { checklist ->
            _checklists.add(checklist)
        }
    }

    /**
     * Adds a new checklist item in [database].
     */
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

    /**
     * Gets all checklists from [database].
     */
    suspend fun fetchChecklists() {
        Timber.d("Fetching checklists from database")
        _checklists.clear()
        _checklists.addAll(database.readChecklists())
    }

    /**
     * Sets the title of a checklist in [database].
     */
    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
        database.updateChecklistTitle(_checklists[checklistIndex].id, title)
    }

    /**
     * Sets an item's check status in [database].
     */
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

    /**
     * Sets an item's name in [database].
     */
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

    /**
     * Sets all items in a checklist to be unchecked in [database].
     */
    fun updateChecklistItems(checklistIndex: Int) {
        _checklists[checklistIndex].items.toMutableList()
            .map { item -> item.copy(isChecked = false) }
            .also { items ->
                _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
                database.updateChecklistItems(_checklists[checklistIndex].id, items)
            }
    }

    /**
     * Sets a checklist to be a favorite in [database].
     */
    fun updateChecklistFavorite(
        checklistIndex: Int,
        isFavorite: Boolean
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(isFavorite = isFavorite)
        database.updateChecklistFavorite(_checklists[checklistIndex].id, isFavorite)
    }

    /**
     * Removes a checklist from [database].
     */
    fun deleteChecklist(checklistIndex: Int) {
        _checklists[checklistIndex].run {
            _checklists.remove(this)
            database.deleteChecklist(id)
        }
    }

    /**
     * Removes an item from a checklist in [database].
     */
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
