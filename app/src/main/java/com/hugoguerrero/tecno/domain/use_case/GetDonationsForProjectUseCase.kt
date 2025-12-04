package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Donation
import com.hugoguerrero.tecno.data.repository.DonationRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDonationsForProjectUseCase @Inject constructor(
    private val repository: DonationRepository
) {
    operator fun invoke(projectId: String): Flow<List<Donation>> {
        return repository.getDonationsForProject(projectId)
    }
}
