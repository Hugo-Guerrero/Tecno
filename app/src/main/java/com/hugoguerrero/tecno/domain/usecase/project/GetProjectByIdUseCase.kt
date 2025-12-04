package com.hugoguerrero.tecno.domain.usecase.project

import com.hugoguerrero.tecno.domain.model.Project
import com.hugoguerrero.tecno.domain.repository.ProjectRepository

class GetProjectByIdUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(id: Int): Project? {
        return repository.getProjectById(id)
    }
}
