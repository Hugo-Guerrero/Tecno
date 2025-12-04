package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.DonationStatus
import com.hugoguerrero.tecno.data.repository.DonationRepository
import javax.inject.Inject

class UpdateDonationStatusUseCase @Inject constructor(
    private val repository: DonationRepository
) {
    suspend operator fun invoke(donationId: String, projectId: String, amount: Double, newStatus: DonationStatus): Result<Unit> {
        return repository.updateDonationStatus(donationId, projectId, amount, newStatus)
    }
}
