package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.domain.model.Document

interface DocumentRepository {
    suspend fun uploadDocument(projectId: Int, document: Document): Boolean
    suspend fun getDocuments(projectId: Int): List<Document>
}
