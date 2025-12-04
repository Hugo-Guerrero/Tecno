package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import javax.inject.Inject

class GetProjectByIdUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String): Project? {
        return projectRepository.getProjectById(projectId)
    }
}
