package com.hugoguerrero.tecno.domain.use_case

import com.google.firebase.auth.FirebaseAuth
import com.hugoguerrero.tecno.data.repository.UserRepository
import com.hugoguerrero.tecno.domain.model.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class RegisterUserUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) {
    operator fun invoke(name: String, email: String, password: String, userType: UserType): Flow<Result<Unit>> = flow {
        try {
            // 1. Crear usuario en Firebase Auth
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("No se pudo crear el usuario.")

            // 2. Crear perfil de usuario en Firestore
            userRepository.createUserProfile(firebaseUser, name, userType)

            emit(Result.success(Unit))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
