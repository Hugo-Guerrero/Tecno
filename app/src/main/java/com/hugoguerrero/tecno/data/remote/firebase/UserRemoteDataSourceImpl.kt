package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hugoguerrero.tecno.data.model.AuthResultDto
import com.hugoguerrero.tecno.data.model.RegisterRequestDto
import com.hugoguerrero.tecno.data.model.UserDto
import com.hugoguerrero.tecno.data.remote.UserRemoteDataSource
import kotlinx.coroutines.tasks.await

class UserRemoteDataSourceImpl(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    override suspend fun login(email: String, password: String): AuthResultDto? {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val user = result.user

            AuthResultDto(
                userId = user?.uid,
                email = user?.email,
                isNewUser = false
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun register(request: RegisterRequestDto): AuthResultDto? {
        return try {
            val result = auth.createUserWithEmailAndPassword(request.email, request.password).await()
            val user = result.user ?: return null

            val userDto = UserDto(
                id = user.uid,
                email = request.email,
                name = request.name,
                photoUrl = null,
                createdAt = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            firestore.collection("users").document(user.uid).set(userDto).await()

            AuthResultDto(
                userId = user.uid,
                email = request.email,
                isNewUser = true
            )
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun getUser(userId: String): UserDto? {
        return try {
            firestore.collection("users")
                .document(userId)
                .get()
                .await()
                .toObject(UserDto::class.java)
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateUser(user: UserDto): Boolean {
        return try {
            firestore.collection("users")
                .document(user.id ?: return false)
                .set(user.copy(updatedAt = System.currentTimeMillis()))
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
