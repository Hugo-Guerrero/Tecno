package com.hugoguerrero.tecno.data.remote.firebase

import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject
import kotlinx.coroutines.tasks.await

class AuthService @Inject constructor(
    private val auth: FirebaseAuth
) {
    suspend fun login(email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password).await()

    suspend fun register(email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password).await()

    fun logout() = auth.signOut()

    fun currentUser() = auth.currentUser
}
