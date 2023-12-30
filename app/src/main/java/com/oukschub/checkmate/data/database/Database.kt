package com.oukschub.checkmate.data.database

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.oukschub.checkmate.data.model.Checklist
import com.oukschub.checkmate.util.FirebaseUtil

class Database {
    private val firestore = Firebase.firestore

    fun addUserToDB() {
        firestore.collection(USERS_COLLECTION)
            .add(FirebaseAuth.getInstance().currentUser!!.uid)
    }

    fun createChecklist(checklist: Checklist) {
        firestore.collection(CHECKLISTS_COLLECTION)
            .add(checklist)
            .addOnSuccessListener { checklistId ->
                firestore.collection(USERS_COLLECTION)
                    .document(FirebaseUtil.getUserId())
                    .update("checklistIds", FieldValue.arrayUnion(checklistId))
            }
    }

    fun loadChecklists(onSuccess: (Checklist) -> Unit) {
        firestore.collection(USERS_COLLECTION)
            .document(FirebaseUtil.getUserId())
            .get()
            .addOnSuccessListener { snapshot ->
                val checklistRefs =
                    snapshot.data?.get("checklistIds") as? ArrayList<DocumentReference>
                if (checklistRefs != null) {
                    for (reference in checklistRefs) {
                        reference.get().addOnSuccessListener {
                            onSuccess(it.toObject<Checklist>()!!)
                        }
                    }
                }
            }
    }

    companion object {
        private const val CHECKLISTS_COLLECTION = "checklists"
        private const val USERS_COLLECTION = "users"
    }
}