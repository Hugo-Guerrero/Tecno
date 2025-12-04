package com.hugoguerrero.tecno.domain.use_case

import android.net.Uri
import com.hugoguerrero.tecno.data.repository.ProjectRepository
import javax.inject.Inject

class CreateProjectUseCase @Inject constructor(
    private val projectRepository: ProjectRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String,
        category: String,
        institution: String,
        fundingGoal: Double,
        ownerUid: String,
        imageUri: Uri,
        documentUris: List<Uri> // Parámetro añadido
    ): Result<Unit> {
        return projectRepository.createProject(
            title = title,
            description = description,
            category = category,
            institution = institution,
            fundingGoal = fundingGoal,
            ownerUid = ownerUid,
            imageUri = imageUri,
            documentUris = documentUris // Pasamos los documentos al repositorio
        )
    }
}
