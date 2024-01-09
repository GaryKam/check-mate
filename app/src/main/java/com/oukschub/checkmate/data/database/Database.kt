package com.oukschub.checkmate.data.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem
import com.oukschub.checkmate.util.FirebaseUtil

class Database {
    private val firestore = Firebase.firestore

    fun addUser() {
        firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .set(mapOf(USER_CHECKLIST_IDS_FIELD to emptyList<DocumentReference>()))
    }

    fun addChecklist(
        title: String,
        items: List<ChecklistItem>,
        onSuccess: () -> Unit
    ) {
        val id = firestore.collection(CHECKLISTS_COLLECTION).document().id

        val checklist = Checklist(
            id = id,
            title = title,
            items = items
        )

        firestore.collection(CHECKLISTS_COLLECTION)
            .document(id)
            .set(checklist)
            .addOnSuccessListener { _ ->
                firestore.collection(USERS_COLLECTION)
                    .document(FirebaseUtil.getUserId())
                    .update(USER_CHECKLIST_IDS_FIELD, FieldValue.arrayUnion(id))
                    .addOnSuccessListener { onSuccess() }
            }
    }

    fun updateChecklist(
        checklist: Checklist,
        onSuccess: () -> Unit
    ) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .document(checklist.id)
            .update(CHECKLIST_TITLE_FIELD, checklist.title)
            .addOnSuccessListener { onSuccess() }
    }

    fun loadChecklists(onSuccess: (Checklist) -> Unit) {
        firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .get()
            .addOnSuccessListener { snapshot ->
                val checklistIds =
                    snapshot.data?.get(USER_CHECKLIST_IDS_FIELD) as? ArrayList<String>
                if (checklistIds != null) {
                    for (id in checklistIds) {
                        firestore.collection(CHECKLISTS_COLLECTION).document(id).get()
                            .addOnSuccessListener { onSuccess(it.toObject<Checklist>()!!) }
                    }
                }
            }
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

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val CHECKLIST_TITLE_FIELD = "title"

        private const val USERS_COLLECTION = "users"
        private const val USER_CHECKLIST_IDS_FIELD = "checklistIds"
    }
}
