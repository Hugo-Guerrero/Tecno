package com.hugoguerrero.tecno.domain.usecase.document

import com.hugoguerrero.tecno.domain.model.Document
import com.hugoguerrero.tecno.domain.repository.DocumentRepository

class GetDocumentsUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(projectId: Int): List<Document> {
        return repository.getDocuments(projectId)
    }
}
