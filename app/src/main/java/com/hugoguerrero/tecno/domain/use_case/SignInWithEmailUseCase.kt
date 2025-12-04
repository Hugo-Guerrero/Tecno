package com.hugoguerrero.tecno.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.hugoguerrero.tecno.domain.repository.AuthRepository // <-- CORREGIDO
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithEmailUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(email: String, password: String): Flow<Result<FirebaseUser>> {
        return authRepository.signInWithEmail(email, password)
    }
}
