package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.domain.model.Project
import com.hugoguerrero.tecno.domain.model.Document

interface ProjectRepository {
    suspend fun getAllProjects(): List<Project>
    suspend fun getProjectById(id: Int): Project?
    suspend fun getProjectsByUser(userId: Int): List<Project>

    suspend fun createProject(
        project: Project,
        documents: List<Document>
    ): Boolean

    suspend fun updateProject(project: Project): Boolean
}
