package com.hugoguerrero.tecno.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.hugoguerrero.tecno.data.model.Complaint
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ComplaintRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val complaintsCollection = firestore.collection("complaints")

    suspend fun createComplaint(complaint: Complaint): Result<Unit> {
        return try {
            val complaintRef = complaintsCollection.document()
            complaintsCollection.document(complaintRef.id).set(complaint.copy(id = complaintRef.id)).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
