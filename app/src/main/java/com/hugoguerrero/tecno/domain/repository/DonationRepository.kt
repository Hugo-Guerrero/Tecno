package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.model.DonationStatus
import kotlinx.coroutines.flow.Flow

interface DonationRepository {

    fun getDonationsForProject(projectId: String): Flow<List<Donation>>

    suspend fun finalizeDonation(donationId: String): Result<Unit>

    suspend fun updateDonationStatus(donationId: String, projectId: String, amount: Double, newStatus: DonationStatus): Result<Unit>
}
