package com.hugoguerrero.tecno.domain.usecase.contribution

import com.hugoguerrero.tecno.domain.model.Contribution
import com.hugoguerrero.tecno.domain.repository.ContributionRepository

class SendContributionUseCase(
    private val repository: ContributionRepository
) {
    suspend operator fun invoke(contribution: Contribution): Boolean {
        return repository.addContribution(contribution)
    }
}
