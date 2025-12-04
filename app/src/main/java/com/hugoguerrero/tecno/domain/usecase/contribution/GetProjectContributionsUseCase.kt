package com.hugoguerrero.tecno.domain.usecase.contribution

import com.hugoguerrero.tecno.domain.model.Contribution
import com.hugoguerrero.tecno.domain.repository.ContributionRepository

class GetProjectContributionsUseCase(
    private val repository: ContributionRepository
) {
    suspend operator fun invoke(projectId: Int): List<Contribution> {
        return repository.getContributionsByProject(projectId)
    }
}
