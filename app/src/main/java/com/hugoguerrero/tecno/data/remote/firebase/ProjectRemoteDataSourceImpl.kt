package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.hugoguerrero.tecno.data.model.ProjectDto
import com.hugoguerrero.tecno.data.remote.ProjectRemoteDataSource
import kotlinx.coroutines.tasks.await

class ProjectRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : ProjectRemoteDataSource {

    private val projectCollection = firestore.collection("projects")

    override suspend fun createProject(project: ProjectDto): Boolean {
        return try {
            val docRef = projectCollection.document()
            val projectWithId = project.copy(id = docRef.id)
            docRef.set(projectWithId).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getProjectById(id: String): ProjectDto? {
        return try {
            val document = projectCollection.document(id).get().await()
            document.toObject(ProjectDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getAllProjects(): List<ProjectDto> {
        return try {
            val snapshot = projectCollection.get().await()
            snapshot.documents.mapNotNull { it.toObject(ProjectDto::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getProjectsByUser(userId: String): List<ProjectDto> {
        return try {
            val snapshot = projectCollection.whereEqualTo("ownerId", userId).get().await()
            snapshot.documents.mapNotNull { it.toObject(ProjectDto::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun updateProjectImage(projectId: String, imageUrl: String): Boolean {
        return try {
            projectCollection.document(projectId)
                .update("imageUrl", imageUrl)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateProjectAmount(projectId: String, amount: Double): Boolean {
        return try {
            projectCollection.document(projectId)
                .update("currentAmount", amount)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
