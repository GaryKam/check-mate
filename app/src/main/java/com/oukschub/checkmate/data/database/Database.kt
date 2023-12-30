package com.oukschub.checkmate.data.database

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
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
                    .update("checklistIds", FieldValue.arrayUnion(checklistId))
            }
    }

    fun loadChecklists(userId: String, onSuccess: (Checklist) -> Unit) {
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .get()
            .addOnSuccessListener { snapshot ->
/*                for (document in snapshot.documents) {
                    val documentRef = document.data?.get("id") as DocumentReference
                    documentRef.get().addOnSuccessListener {
                        onSuccess(it.toObject<Checklist>()!!)
                    }
                }*/
                val checklistIds = snapshot.data?.get("checklistIds")
                Log.d("HIII", checklistIds.toString())
                for (id in checklistIds){
                    onSuccess(id.toObject<Checklist>()!!)
                }
            }
    }

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val USERS_COLLECTION = "users"
    }
}