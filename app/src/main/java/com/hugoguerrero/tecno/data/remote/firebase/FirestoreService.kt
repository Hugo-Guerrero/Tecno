package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.hugoguerrero.tecno.data.model.ProjectDto
import com.hugoguerrero.tecno.data.model.UserDto
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class FirestoreService @Inject constructor(
    private val db: FirebaseFirestore
) {

    suspend fun getUser(uid: String): UserDto? {
        return db.collection("users")
            .document(uid)
            .get()
            .await()
            .toObject(UserDto::class.java)
    }

    suspend fun saveProject(project: ProjectDto) {
        project.id?.let { projectId -> // Comprobación segura
            db.collection("projects")
                .document(projectId)
                .set(project)
                .await()
        }
    }
}
