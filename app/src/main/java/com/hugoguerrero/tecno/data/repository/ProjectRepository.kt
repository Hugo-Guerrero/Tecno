package com.hugoguerrero.tecno.data.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.hugoguerrero.tecno.data.model.Project
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID

class ProjectRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val projectsCollection = firestore.collection("projects")

    fun getProjects(): Flow<List<Project>> = callbackFlow {
        val listener = projectsCollection.orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val projects = snapshot.toObjects(Project::class.java)
                    trySend(projects)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun getProjectById(projectId: String): Project? {
        return try {
            projectsCollection.document(projectId).get().await().toObject(Project::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun observeProjectById(projectId: String): Flow<Project?> = callbackFlow {
        val documentRef = projectsCollection.document(projectId)
        val listener = documentRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null && snapshot.exists()) {
                trySend(snapshot.toObject(Project::class.java))
            } else {
                trySend(null)
            }
        }
        awaitClose { listener.remove() }
    }

    fun getProjectsByOwner(ownerUid: String): Flow<List<Project>> = callbackFlow {
        val listener = projectsCollection.whereEqualTo("ownerUid", ownerUid)
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    close(e)
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    val projects = snapshot.toObjects(Project::class.java)
                    trySend(projects)
                }
            }
        awaitClose { listener.remove() }
    }

    suspend fun createProject(
        title: String,
        description: String,
        category: String,
        institution: String,
        fundingGoal: Double,
        ownerUid: String,
        imageUri: Uri,
        documentUris: List<Uri>
    ): Result<Unit> {
        return try {
            val projectId = projectsCollection.document().id
            val imageUrl = uploadProjectImage(imageUri, projectId)
            val documentUrls = uploadProjectDocuments(documentUris, projectId)

            val newProject = Project(
                id = projectId,
                title = title,
                description = description,
                category = category,
                institution = institution,
                fundingGoal = fundingGoal,
                ownerUid = ownerUid,
                imageUrl = imageUrl,
                documentUrls = documentUrls
            )
            projectsCollection.document(projectId).set(newProject).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProject(
        projectId: String,
        title: String,
        description: String,
        category: String,
        institution: String,
        fundingGoal: Double,
        newImageUri: Uri?,
        newDocumentUris: List<Uri>?
    ): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>(
                "title" to title,
                "description" to description,
                "category" to category,
                "institution" to institution,
                "fundingGoal" to fundingGoal
            )

            newImageUri?.let {
                val newImageUrl = uploadProjectImage(it, projectId)
                updates["imageUrl"] = newImageUrl
            }

            newDocumentUris?.let {
                if (it.isNotEmpty()) {
                    val newDocumentUrls = uploadProjectDocuments(it, projectId)
                    updates["documentUrls"] = newDocumentUrls // Idealmente, esto debería añadir a los existentes
                }
            }

            projectsCollection.document(projectId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    suspend fun deleteProject(projectId: String): Result<Unit> {
        return try {
            projectsCollection.document(projectId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadProjectImage(imageUri: Uri, projectId: String): String {
        val storageRef = storage.reference.child("project_images/$projectId/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(imageUri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }

    private suspend fun uploadProjectDocuments(documentUris: List<Uri>, projectId: String): List<String> {
        val downloadUrls = mutableListOf<String>()
        for (uri in documentUris) {
            val storageRef = storage.reference.child("project_documents/$projectId/${UUID.randomUUID()}")
            val uploadTask = storageRef.putFile(uri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await().toString()
            downloadUrls.add(downloadUrl)
        }
        return downloadUrls
    }
}
