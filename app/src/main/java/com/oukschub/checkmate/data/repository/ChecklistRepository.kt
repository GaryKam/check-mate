package com.oukschub.checkmate.data.repository

import androidx.compose.runtime.mutableStateListOf
import com.google.common.collect.ImmutableList
import com.oukschub.checkmate.data.database.Database
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.util.ChecklistType
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

    /** Index of checklist most recently focused. */
    var currentChecklist = -1

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
     * Allows user to see latest item position while dragging. Modifies local [_checklists].
     */
    fun moveChecklistItem(
        checklistIndex: Int,
        fromIndex: Int,
        toIndex: Int
    ) {
        _checklists[checklistIndex].items.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
        }
    }

    /**
     * Removes all checklist data. Modifies local [_checklists].
     */
    fun clearChecklists() {
        _checklists.clear()
        database.reset()
    }

    /**
     * Adds a new checklist.
     */
    suspend fun createChecklist(
        title: String,
        items: List<ChecklistItem>
    ): Boolean {
        val checklist = database.createChecklist(title, items)
        checklist?.let { _checklists.add(it) }
        return checklist != null
    }

    /**
     * Adds an existing checklist created by another user.
     */
    suspend fun createChecklist(shareCode: String): Boolean {
        val sharedChecklist = database.createSharedChecklist(shareCode)
        sharedChecklist?.let { _checklists.add(it) }
        return sharedChecklist != null
    }

    /**
     * Adds a new checklist item.
     */
    fun createChecklistItem(
        checklistIndex: Int,
        itemName: String,
        isDivider: Boolean = false
    ) {
        val item = ChecklistItem(name = itemName, isDivider = isDivider)
        _checklists[checklistIndex].items.toMutableList().apply {
            add(item)
        }.also { items ->
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.createChecklistItem(_checklists[checklistIndex].id, item)
        }

        updateChecklistDividers(checklistIndex)
    }

    suspend fun fetchSharedChecklists() {
        Timber.d("Start fetching shared checklists from database")
        val startTime = System.currentTimeMillis()
        database.readChecklists(ChecklistType.SHARED)
        val endTime = (System.currentTimeMillis() - startTime) / 1000.0
        Timber.d("Finish fetching shared checklists from database: $endTime")
    }

    /**
     * Gets all favorite checklists.
     */
    suspend fun fetchFavoriteChecklists() {
        Timber.d("Start fetching favorite checklists from database")
        val startTime = System.currentTimeMillis()
        _checklists.addAll(database.readChecklists(ChecklistType.FAVORITE))
        val endTime = (System.currentTimeMillis() - startTime) / 1000.0
        Timber.d("Finish fetching favorite checklists from database: $endTime")
    }

    /**
     * Gets all regular checklists.
     */
    suspend fun fetchChecklists() {
        Timber.d("Start fetching checklists from database")
        val startTime = System.currentTimeMillis()
        _checklists.addAll(database.readChecklists(ChecklistType.DEFAULT))
        val endTime = (System.currentTimeMillis() - startTime) / 1000.0
        Timber.d("Finish fetching checklists from database: $endTime")
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
        val isDivider = _checklists[checklistIndex].items[itemIndex].isDivider

        if (isDivider) {
            updateChecklistDivider(checklistIndex, itemIndex, isChecked)
        } else {
            _checklists[checklistIndex].items.toMutableList().apply {
                this[itemIndex] = this[itemIndex].copy(isChecked = isChecked)
            }.also { items ->
                _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
                database.updateChecklistItems(_checklists[checklistIndex].id, items)
            }

            updateChecklistDividers(checklistIndex)
        }
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
     * Sets the positions of all items after user has finished dragging.
     */
    fun updateChecklistItemPositions(checklistIndex: Int) {
        val items = _checklists[checklistIndex].items
        database.updateChecklistItems(_checklists[checklistIndex].id, items)

        updateChecklistDividers(checklistIndex)
    }

    /**
     * Sets a divider's check status, and all items below it.
     */
    private fun updateChecklistDivider(
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
     * Sets the checklist items upon pausing the application.
     */
    fun updateChecklistOnPause() {
        if (currentChecklist != -1) {
            val checklist = _checklists[currentChecklist]
            database.updateChecklistItems(checklist.id, checklist.items)
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
     * Sets a code to give other users access to the checklist.
     */
    fun updateChecklistShare(
        checklistIndex: Int,
        shareCode: String
    ) {
        database.updateChecklistShareCode(_checklists[checklistIndex].id, shareCode)
    }

    /**
     * Removes a checklist.
     */
    fun deleteChecklist(
        checklistIndex: Int,
        isShared: Boolean
    ) {
        _checklists[checklistIndex].run {
            _checklists.remove(this)

            if (isShared) {
                database.deleteChecklistFromUser(id)
            } else {
                database.deleteChecklist(id)
            }
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

        if (dividerIndex != -1 && items[dividerIndex].isChecked != dividerState) {
            items[dividerIndex] = items[dividerIndex].copy(isChecked = dividerState)
            requireUpdate = true
        }

        if (requireUpdate) {
            _checklists[checklistIndex] = _checklists[checklistIndex].copy(items = items)
            database.updateChecklistItems(_checklists[checklistIndex].id, items)
        }
    }
}
