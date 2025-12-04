package com.hugoguerrero.tecno.domain.usecase.document

import com.hugoguerrero.tecno.domain.model.Document
import com.hugoguerrero.tecno.domain.repository.DocumentRepository

class UploadDocumentUseCase(
    private val repository: DocumentRepository
) {
    suspend operator fun invoke(projectId: Int, document: Document): Boolean {
        return repository.uploadDocument(projectId, document)
    }
}
