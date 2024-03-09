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
     * Adds a new checklist.
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
     * Adds a new checklist item.
     */
    fun createChecklistItem(
        checklistIndex: Int,
        itemName: String,
        isDivider: Boolean = false
    ) {
        val item = ChecklistItem(
            name = itemName,
            isDivider = isDivider
        )
        _checklists[checklistIndex].items.toMutableList().apply {
            add(item)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.createChecklistItem(_checklists[checklistIndex].id, item)
        }

        updateChecklistDividers(checklistIndex)
    }

    /**
     * Gets all checklists.
     */
    suspend fun fetchChecklists() {
        Timber.d("Fetching checklists from database")
        _checklists.clear()
        _checklists.addAll(database.readChecklists())
    }

    /**
     * Sets the title of a checklist.
     */
    fun updateChecklistTitle(
        checklistIndex: Int,
        title: String
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(title = title)
        database.updateChecklistTitle(_checklists[checklistIndex].id, title)
    }

    /**
     * Sets an item's check status.
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

        updateChecklistDividers(checklistIndex)
    }

    /**
     * Sets an item's name.
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
     * Sets all items in a checklist to be unchecked.
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
     * Sets a divider's check status, and all items below it.
     */
    fun updateChecklistDivider(
        checklistIndex: Int,
        dividerIndex: Int,
        isChecked: Boolean
    ) {
        _checklists[checklistIndex].items.toMutableList().apply {
            this[dividerIndex] = this[dividerIndex].copy(isChecked = isChecked)

            for (i in dividerIndex + 1..<this.size) {
                if (this[i].isDivider) {
                    break
                } else {
                    this[i] = this[i].copy(isChecked = isChecked)
                }
            }
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.updateChecklistItems(_checklists[checklistIndex].id, items)
        }
    }

    /**
     * Sets a checklist to be a favorite.
     */
    fun updateChecklistFavorite(
        checklistIndex: Int,
        isFavorite: Boolean
    ) {
        _checklists[checklistIndex] = _checklists[checklistIndex].copy(isFavorite = isFavorite)
        database.updateChecklistFavorite(_checklists[checklistIndex].id, isFavorite)
    }

    /**
     * Removes a checklist.
     */
    fun deleteChecklist(checklistIndex: Int) {
        _checklists[checklistIndex].run {
            _checklists.remove(this)
            database.deleteChecklist(id)
        }
    }

    /**
     * Removes an item from a checklist.
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

        updateChecklistDividers(checklistIndex)
    }

    /**
     * Sets the check status of all dividers according to the check status of items below.
     */
    private fun updateChecklistDividers(checklistIndex: Int) {
        val items = _checklists[checklistIndex].items.toMutableList()
        var requireUpdate = false
        var dividerState = true
        var dividerIndex = -1

        for ((i, item) in items.withIndex()) {
            if (item.isDivider) {
                if (dividerIndex != -1) {
                    if (items[dividerIndex].isChecked != dividerState) {
                        items[dividerIndex] = items[dividerIndex].copy(isChecked = dividerState)
                        requireUpdate = true
                    }
                }

                dividerIndex = i
                dividerState = true
            } else if (dividerIndex != -1) {
                if (!item.isChecked) {
                    dividerState = false
                }
            }
        }

        if (items[dividerIndex].isChecked != dividerState) {
            items[dividerIndex] = items[dividerIndex].copy(isChecked = dividerState)
            requireUpdate = true
        }

        if (requireUpdate) {
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.updateChecklistItems(_checklists[checklistIndex].id, items)
        }
    }
}
