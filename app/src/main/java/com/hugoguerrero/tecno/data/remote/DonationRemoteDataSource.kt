package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.DonationDto

interface DonationRemoteDataSource {
    suspend fun addDonation(donation: DonationDto): Boolean
    suspend fun getDonationsByProject(projectId: String): List<DonationDto>
    suspend fun getDonationsByUser(userId: String): List<DonationDto> // ✅ Agregada
}