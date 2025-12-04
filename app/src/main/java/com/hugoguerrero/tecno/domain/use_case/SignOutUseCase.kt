package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.signOut()
    }
}
