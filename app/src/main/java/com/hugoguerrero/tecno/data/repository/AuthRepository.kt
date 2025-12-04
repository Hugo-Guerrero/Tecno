package com.hugoguerrero.tecno.data.repository

import android.app.Application
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hugoguerrero.tecno.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val firebaseAuth: FirebaseAuth,
    private val application: Application
) {

    // Opciones para obtener el GoogleSignInClient
    private val gso: GoogleSignInOptions
        get() = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application.getString(R.string.web_client_id))
            .requestEmail()
            .build()

    private val googleSignInClient by lazy { GoogleSignIn.getClient(application, gso) }

    // ... (signInWithGoogle y signInWithEmail no cambian)

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    // Función de signOut actualizada
    suspend fun signOut() {
        try {
            // Cierra la sesión de Firebase
            firebaseAuth.signOut()
            // Cierra la sesión de Google
            googleSignInClient.signOut().await()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
