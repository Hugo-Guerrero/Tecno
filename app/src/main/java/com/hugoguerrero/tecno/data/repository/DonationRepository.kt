package com.hugoguerrero.tecno.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.model.DonationStatus
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class DonationRepository @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val donationsCollection = firestore.collection("donations")
    private val projectsCollection = firestore.collection("projects")


    suspend fun updateDonationStatus(donationId: String, projectId: String, amount: Double, newStatus: DonationStatus): Result<Unit> {
        return try {
            val donationRef = donationsCollection.document(donationId)
            val projectRef = projectsCollection.document(projectId)

            firestore.runTransaction {
                transaction ->
                val snapshot = transaction.get(donationRef)
                val currentStatus = snapshot.get("status", DonationStatus::class.java)

                if (currentStatus == DonationStatus.PENDING) {
                    transaction.update(donationRef, "status", newStatus)
                    if (newStatus == DonationStatus.COMPLETED) {
                        transaction.update(projectRef, "currentFunding", FieldValue.increment(amount))
                    }
                }
                null
            }.await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getDonationsForProject(projectId: String): Flow<List<Donation>> = callbackFlow {
        val listenerRegistration = donationsCollection
            .whereEqualTo("projectId", projectId)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val donations = snapshot.documents.mapNotNull { doc ->
                        doc.toObject<Donation>()?.copy(id = doc.id)
                    }
                    trySend(donations)
                }
            }
        awaitClose { listenerRegistration.remove() }
    }
    
    suspend fun createDonation(donation: Donation): Result<String> {
        return try {
            val newDonation = donation.copy(id = donationsCollection.document().id)
            donationsCollection.document(newDonation.id).set(newDonation).await()
            Result.success(newDonation.id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
