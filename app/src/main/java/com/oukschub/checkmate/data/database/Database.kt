package com.oukschub.checkmate.data.database

import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.oukschub.checkmate.data.model.Checklist

class Database {
    private val firestore = Firebase.firestore

    fun createChecklist(checklist: Checklist, userId: String) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .add(checklist)
            .addOnSuccessListener { checklistId ->
                firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(CHECKLISTS_COLLECTION)
                    .add(mapOf("id" to checklistId))
            }
    }

    fun loadChecklists(userId: String, onSuccess: (Checklist) -> Unit) {
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(CHECKLISTS_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                for (document in snapshot.documents) {
                    val documentRef = document.data?.get("id") as DocumentReference
                    documentRef.get().addOnSuccessListener {
                        onSuccess(it.toObject<Checklist>()!!)
                    }
                }
            }
    }

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val USERS_COLLECTION = "users"
    }
}