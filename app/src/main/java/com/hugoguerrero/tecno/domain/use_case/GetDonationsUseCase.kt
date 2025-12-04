package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDonationsUseCase @Inject constructor(
    private val donationRepository: DonationRepository
) {
    operator fun invoke(projectId: String): Flow<List<Donation>> {
        return donationRepository.getDonationsForProject(projectId)
    }
}
