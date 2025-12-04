package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.hugoguerrero.tecno.data.model.DonationDto
import com.hugoguerrero.tecno.data.remote.DonationRemoteDataSource
import kotlinx.coroutines.tasks.await

class DonationRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : DonationRemoteDataSource {

    private val donationsRef = firestore.collection("donations")


    override suspend fun addDonation(donation: DonationDto): Boolean {
        return try {
            val newDoc = donationsRef.document()
            val donationWithId = donation.copy(id = newDoc.id)
            newDoc.set(donationWithId).await()
            true
        } catch (e: Exception) {
            false
        }
    }


    override suspend fun getDonationsByProject(projectId: String): List<DonationDto> {
        return try {
            donationsRef
                .whereEqualTo("projectId", projectId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(DonationDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getDonationsByUser(userId: String): List<DonationDto> {
        return try {
            donationsRef
                .whereEqualTo("userId", userId)
                .orderBy("createdAt", Query.Direction.DESCENDING)
                .get()
                .await()
                .toObjects(DonationDto::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
