package com.oukschub.checkmate.data.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.util.FirebaseUtil
import kotlinx.coroutines.tasks.await

/**
 * Firestore database to handle checklist and user data.
 */
class Database {
    private val firestore = Firebase.firestore

    fun createUser(displayName: String) {
        firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .set(
                mapOf(
                    USER_CHECKLIST_IDS_FIELD to emptyList<DocumentReference>(),
                    USER_CHECKLIST_FAVORITES_FIELD to emptyList<DocumentReference>()
                )
            )

        FirebaseAuth.getInstance().currentUser?.updateProfile(
            UserProfileChangeRequest.Builder().setDisplayName(displayName).build()
        )
    }

    fun createChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ): Checklist {
        val id = firestore.collection(CHECKLISTS_COLLECTION).document().id
        val checklist = Checklist(id, title, items)

        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .set(checklist)
            .addOnSuccessListener { _ ->
                firestore.collection(USERS_COLLECTION)
                    .document(FirebaseUtil.getUserId())
                    .update(USER_CHECKLIST_IDS_FIELD, FieldValue.arrayUnion(id))
                    .addOnSuccessListener { onSuccess() }
            }

        return checklist
    }

    fun createChecklistItem(
        id: String,
        item: ChecklistItem
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .update(
                CHECKLIST_ITEMS_FIELD,
                FieldValue.arrayUnion(item)
            )
    }

    suspend fun fetchChecklists(): List<Checklist> {
        val result = firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .get()
            .await()

        val checklists = mutableListOf<Checklist>()

        @Suppress("UNCHECKED_CAST")
        val favoriteChecklistIds =
            (result.data?.get(USER_CHECKLIST_FAVORITES_FIELD) as ArrayList<String>)

        @Suppress("UNCHECKED_CAST")
        (result.data?.get(USER_CHECKLIST_IDS_FIELD) as ArrayList<String>).let { ids ->
            for (id in ids) {
                val checklist = firestore.collection(CHECKLISTS_COLLECTION)
                    .document(id)
                    .get()
                    .await()
                    .toObject<Checklist>()!!

                if (favoriteChecklistIds.contains(id)) {
                    checklists.add(checklist.copy(isFavorite = true))
                } else {
                    checklists.add(checklist)
                }
            }
        }

        return checklists
    }

    fun updateChecklistTitle(
        id: String,
        title: String,
        onSuccess: () -> Unit
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .update(CHECKLIST_TITLE_FIELD, title)
            .addOnSuccessListener { onSuccess() }
    }

    fun updateChecklistItems(
        id: String,
        items: List<ChecklistItem>
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .update(CHECKLIST_ITEMS_FIELD, items)
    }

    fun updateChecklistFavorite(
        id: String,
        isFavorite: Boolean
    ) {
        firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .update(
                USER_CHECKLIST_FAVORITES_FIELD,
                if (isFavorite) {
                    FieldValue.arrayUnion(id)
                } else {
                    FieldValue.arrayRemove(id)
                }
            )
    }

    fun deleteChecklist(
        id: String,
        onSuccess: () -> Unit
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .delete()
            .addOnSuccessListener {
                firestore.collection(USERS_COLLECTION)
                    .document(FirebaseUtil.getUserId())
                    .update(USER_CHECKLIST_IDS_FIELD, FieldValue.arrayRemove(id))
                    .addOnSuccessListener { onSuccess() }
            }
    }

    fun deleteChecklistItem(
        id: String,
        item: ChecklistItem
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .update(
                CHECKLIST_ITEMS_FIELD,
                FieldValue.arrayRemove(item)
            )
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
