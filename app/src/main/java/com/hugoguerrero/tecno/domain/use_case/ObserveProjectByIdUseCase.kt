package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveProjectByIdUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(projectId: String): Flow<Project?> {
        return projectRepository.observeProjectById(projectId)
    }
}
