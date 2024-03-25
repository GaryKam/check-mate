package com.oukschub.checkmate.data.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.util.ChecklistType
import kotlinx.coroutines.tasks.await
import timber.log.Timber

/**
 * Firestore database to handle checklist and user data.
 */
class Database {
    private val firestore = Firebase.firestore
    private val userId: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid
    private val favoriteChecklistIds = mutableListOf<String>()

    suspend fun createUser(): Boolean {
        val task = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .set(
                mapOf<String, List<DocumentReference>>(
                    USER_CHECKLIST_IDS_FIELD to emptyList(),
                    USER_CHECKLIST_FAVORITES_FIELD to emptyList()
                )
            )
            .also { it.await() }

        return task.isSuccessful
    }

    suspend fun createChecklist(
        title: String,
        items: List<ChecklistItem>
    ): Checklist? {
        val id = firestore.collection(CHECKLISTS_COLLECTION).document().id
        val checklist = Checklist(id, title, items)

        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .set(checklist)
            .await()

        val task = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update(USER_CHECKLIST_IDS_FIELD, FieldValue.arrayUnion(id))
            .also { it.await() }

        return if (task.isSuccessful) {
            Timber.d("Created checklist: $title")
            checklist
        } else {
            Timber.d("Failed to create checklist: $title")
            null
        }
    }

    fun createChecklistItem(
        checklistId: String,
        item: ChecklistItem
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklistId)
            .update(CHECKLIST_ITEMS_FIELD, FieldValue.arrayUnion(item))
            .addOnSuccessListener { Timber.d("Created item: ${item.name}") }
            .addOnFailureListener { Timber.d("Failed to create item: ${item.name}") }
    }

    suspend fun readChecklists(checklistType: ChecklistType): List<Checklist> {
        val result = firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .await()

        val checklists = mutableListOf<Checklist>()

        val checklistIds = when (checklistType) {
            ChecklistType.DEFAULT -> {
                @Suppress("UNCHECKED_CAST")
                val ids = result.data?.get(USER_CHECKLIST_IDS_FIELD) as ArrayList<String>
                ids.removeIf { favoriteChecklistIds.contains(it) }
                ids
            }

            ChecklistType.FAVORITE -> {
                @Suppress("UNCHECKED_CAST")
                val ids = result.data?.get(USER_CHECKLIST_FAVORITES_FIELD) as ArrayList<String>
                favoriteChecklistIds.clear()
                favoriteChecklistIds.addAll(ids)
                ids
            }
        }

        checklistIds.let { ids ->
            for (id in ids) {
                val checklist = firestore.collection(CHECKLISTS_COLLECTION)
                    .document(id)
                    .get()
                    .await()
                    .toObject<Checklist>()!!

                when (checklistType) {
                    ChecklistType.DEFAULT -> checklists.add(checklist)
                    ChecklistType.FAVORITE -> checklists.add(checklist.copy(isFavorite = true))
                }
            }
        }

        return checklists
    }

    fun updateChecklistTitle(
        checklistId: String,
        title: String
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklistId)
            .update(CHECKLIST_TITLE_FIELD, title)
            .addOnSuccessListener { Timber.d("Updated title: $title") }
            .addOnFailureListener { Timber.d("Failed to update title: $title") }
    }

    fun updateChecklistItems(
        checklistId: String,
        items: List<ChecklistItem>
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklistId)
            .update(CHECKLIST_ITEMS_FIELD, items)
            .addOnSuccessListener { Timber.d("Updated items: $items") }
            .addOnFailureListener { Timber.d("Failed to update items: $items") }
    }

    fun updateChecklistFavorite(
        checklistId: String,
        isFavorite: Boolean
    ) {
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .update(
                USER_CHECKLIST_FAVORITES_FIELD,
                if (isFavorite) {
                    FieldValue.arrayUnion(checklistId)
                } else {
                    FieldValue.arrayRemove(checklistId)
                }
            )
            .addOnSuccessListener { Timber.d("Updated favorite status: $checklistId") }
            .addOnFailureListener { Timber.d("Failed to update favorite status: $checklistId") }
    }

    fun deleteChecklist(checklistId: String) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklistId)
            .delete()
            .addOnSuccessListener {
                Timber.d("Deleted checklist from database: $checklistId")
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .update(USER_CHECKLIST_IDS_FIELD, FieldValue.arrayRemove(checklistId))
                    .addOnSuccessListener { Timber.d("Delete checklist from user: $checklistId") }
                    .addOnFailureListener { Timber.d("Failed to delete checklist from user: $checklistId") }

                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .update(USER_CHECKLIST_FAVORITES_FIELD, FieldValue.arrayRemove(checklistId))
                    .addOnSuccessListener { Timber.d("Delete checklist from user favorites: $checklistId") }
                    .addOnFailureListener { Timber.d("Failed to delete checklist from user favorites: $checklistId") }
            }
            .addOnFailureListener { Timber.d("Failed to delete checklist: $checklistId") }
    }

    fun deleteChecklistItem(
        checklistId: String,
        item: ChecklistItem
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklistId)
            .update(
                CHECKLIST_ITEMS_FIELD,
                FieldValue.arrayRemove(item)
            )
            .addOnSuccessListener { Timber.d("Deleted item: ${item.name}") }
            .addOnFailureListener { Timber.d("Failed to delete item: ${item.name}") }
    }

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val CHECKLIST_TITLE_FIELD = "title"
        private const val CHECKLIST_ITEMS_FIELD = "items"

        private const val USERS_COLLECTION = "users"
        private const val USER_CHECKLIST_IDS_FIELD = "checklistIds"
        private const val USER_CHECKLIST_FAVORITES_FIELD = "favoriteIds"
    }
}
