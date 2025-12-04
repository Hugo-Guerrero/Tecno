package com.hugoguerrero.tecno.domain.usecase.project

import com.hugoguerrero.tecno.domain.model.Project
import com.hugoguerrero.tecno.domain.repository.ProjectRepository

class GetUserProjectsUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(userId: Int): List<Project> {
        return repository.getProjectsByUser(userId)
    }
}
