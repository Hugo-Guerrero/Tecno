package com.hugoguerrero.tecno.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.domain.model.UserType
import kotlinx.coroutines.tasks.await
import java.util.UUID

class UserRepository(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val usersCollection = firestore.collection("users")

    suspend fun createUserProfile(user: FirebaseUser, name: String, userType: UserType) {
        val userProfile = UserProfile(
            uid = user.uid,
            displayName = name, 
            email = user.email ?: "",
            photoUrl = user.photoUrl?.toString() ?: "",
            userType = userType 
        )
        usersCollection.document(user.uid).set(userProfile).await()
    }

    suspend fun getUserProfile(uid: String): UserProfile? {
        return try {
            usersCollection.document(uid).get().await().toObject(UserProfile::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun userProfileExists(uid: String): Boolean {
        return try {
            usersCollection.document(uid).get().await().exists()
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateFcmToken(userId: String, token: String) {
        usersCollection.document(userId).update("fcmToken", token).await()
    }

    suspend fun updateUserProfile(uid: String, displayName: String, bio: String, photoUri: Uri?, paymentDetails: String): Result<Unit> {
        return try {
            val updates = mutableMapOf<String, Any>()
            updates["displayName"] = displayName
            updates["bio"] = bio
            updates["paymentDetails"] = paymentDetails

            photoUri?.let {
                val imageUrl = uploadProfilePhoto(uid, it)
                updates["photoUrl"] = imageUrl
            }

            usersCollection.document(uid).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun uploadProfilePhoto(uid: String, photoUri: Uri): String {
        val storageRef = storage.reference.child("profile_photos/$uid/${UUID.randomUUID()}")
        val uploadTask = storageRef.putFile(photoUri).await()
        return uploadTask.storage.downloadUrl.await().toString()
    }
}
