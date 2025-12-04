package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.model.DonationStatus
import com.hugoguerrero.tecno.data.repository.DonationRepository
import javax.inject.Inject

class CreateDonationUseCase @Inject constructor(
    private val donationRepository: DonationRepository
) {
    suspend operator fun invoke(
        projectId: String,
        donorUid: String,
        amount: Double,
        message: String,
        status: DonationStatus,
        proofImageUrl: String
    ): Result<String> {
        val donation = Donation(
            projectId = projectId,
            donorUid = donorUid,
            amount = amount,
            message = message,
            status = status,
            proofImageUrl = proofImageUrl
        )
        return donationRepository.createDonation(donation)
    }
}
