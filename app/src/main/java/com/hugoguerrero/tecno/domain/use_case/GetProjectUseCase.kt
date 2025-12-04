package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.ProjectRepository
import javax.inject.Inject

class GetProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(projectId: String) = repository.getProjectById(projectId)
}
