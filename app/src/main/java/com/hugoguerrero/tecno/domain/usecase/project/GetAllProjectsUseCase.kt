package com.hugoguerrero.tecno.domain.usecase.project

import com.hugoguerrero.tecno.domain.model.Project
import com.hugoguerrero.tecno.domain.repository.ProjectRepository

class GetAllProjectsUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(): List<Project> {
        return repository.getAllProjects()
    }
}
