package com.hugoguerrero.tecno.domain.use_case

import android.net.Uri
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import javax.inject.Inject

class UpdateProjectUseCase @Inject constructor(
    private val repository: ProjectRepository
) {
    suspend operator fun invoke(
        projectId: String,
        title: String,
        description: String,
        category: String,
        institution: String,
        fundingGoal: Double,
        newImageUri: Uri?,
        newDocumentUris: List<Uri>?
    ): Result<Unit> {
        return repository.updateProject(
            projectId = projectId,
            title = title,
            description = description,
            category = category,
            institution = institution,
            fundingGoal = fundingGoal,
            newImageUri = newImageUri,
            newDocumentUris = newDocumentUris
        )
    }
}
