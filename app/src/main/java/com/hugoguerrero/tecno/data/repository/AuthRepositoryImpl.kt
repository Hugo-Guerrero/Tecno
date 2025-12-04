package com.hugoguerrero.tecno.data.repository

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hugoguerrero.tecno.R
import com.hugoguerrero.tecno.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val application: Application
) : AuthRepository {

    private val gso: GoogleSignInOptions
        get() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.web_client_id))
            .requestEmail()
            .build()

    private val googleSignInClient by lazy { GoogleSignIn.getClient(application, gso) }

    override fun signInWithEmail(email: String, password: String): Flow<Result<FirebaseUser>> = flow {
        try {
            val userCredential = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = userCredential.user!!
            emit(Result.success(user))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun signInWithGoogle(credential: AuthCredential): Flow<Result<FirebaseUser>> = flow {
        try {
            val userCredential = firebaseAuth.signInWithCredential(credential).await()
            val user = userCredential.user!!
            emit(Result.success(user))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    override suspend fun signOut() {
        try {
            firebaseAuth.signOut()
            googleSignInClient.signOut().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
