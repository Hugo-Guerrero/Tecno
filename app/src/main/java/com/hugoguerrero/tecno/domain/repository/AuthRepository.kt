package com.hugoguerrero.tecno.domain.repository

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun signInWithEmail(email: String, password: String): Flow<Result<FirebaseUser>>
    fun signInWithGoogle(credential: AuthCredential): Flow<Result<FirebaseUser>>
    fun getCurrentUser(): FirebaseUser?
    suspend fun signOut()
}
