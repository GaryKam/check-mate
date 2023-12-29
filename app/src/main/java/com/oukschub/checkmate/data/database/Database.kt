package com.oukschub.checkmate.data.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.data.model.ChecklistItem

class Database {
    private val firestore = Firebase.firestore
    private val checklistIds = mutableListOf<String>()

    init {
        firestore.collection(USERS_COLLECTION)
            .document("admin")
            .get()
            .addOnSuccessListener {
                Log.d("fiefie", it.data.toString())
            }
    }

    fun createChecklist(checklist: Checklist, userId: String) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .add(checklist)
            .addOnSuccessListener { checklistId ->
                val userChecklistRef = firestore.collection(USERS_COLLECTION)
                    .document(userId)
                    .collection(CHECKLISTS_COLLECTION)
                userChecklistRef.add(mapOf(checklist.title to checklistId))
            }
    }

    fun addToChecklist(checklist: String, item: ChecklistItem) {
        firestore.collection(CHECKLISTS_COLLECTION).add(item)
    }

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val USERS_COLLECTION = "users"
    }
}