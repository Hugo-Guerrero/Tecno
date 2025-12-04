package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.domain.model.Contribution

interface ContributionRepository {
    suspend fun getContributionsByProject(projectId: Int): List<Contribution>
    suspend fun addContribution(contribution: Contribution): Boolean
}
