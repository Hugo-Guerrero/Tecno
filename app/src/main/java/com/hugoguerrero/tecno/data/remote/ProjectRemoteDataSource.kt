package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.ProjectDto

interface ProjectRemoteDataSource {
    suspend fun createProject(project: ProjectDto): Boolean
    suspend fun getProjectById(id: String): ProjectDto?
    suspend fun getAllProjects(): List<ProjectDto>
    suspend fun getProjectsByUser(userId: String): List<ProjectDto>
    suspend fun updateProjectImage(projectId: String, imageUrl: String): Boolean
    suspend fun updateProjectAmount(projectId: String, amount: Double): Boolean
}