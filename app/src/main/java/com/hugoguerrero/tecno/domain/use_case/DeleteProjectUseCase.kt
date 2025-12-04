package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.ProjectRepository
import javax.inject.Inject

class DeleteProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String): Result<Unit> {
        return projectRepository.deleteProject(projectId)
    }
}
