package com.hugoguerrero.tecno.domain.use_case

import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.hugoguerrero.tecno.domain.repository.AuthRepository // <-- ¡CORREGIDO!
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SignInWithGoogleUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke(credential: AuthCredential): Flow<Result<FirebaseUser>> {
        return authRepository.signInWithGoogle(credential)
    }
}
