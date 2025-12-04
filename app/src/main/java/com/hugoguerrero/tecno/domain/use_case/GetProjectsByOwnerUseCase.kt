package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.Project
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProjectsByOwnerUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    operator fun invoke(ownerUid: String): Flow<List<Project>> {
        return projectRepository.getProjectsByOwner(ownerUid)
    }
}
