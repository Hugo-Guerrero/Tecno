package com.hugoguerrero.tecno.domain.usecase.project

import com.hugoguerrero.tecno.domain.model.Document
import com.hugoguerrero.tecno.domain.model.Project
import com.hugoguerrero.tecno.domain.repository.ProjectRepository

class CreateProjectUseCase(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(project: Project, documents: List<Document>): Boolean {
        return repository.createProject(project, documents)
    }
}
