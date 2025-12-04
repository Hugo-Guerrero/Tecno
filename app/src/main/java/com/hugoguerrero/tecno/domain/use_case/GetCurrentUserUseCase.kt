package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    operator fun invoke() = authRepository.getCurrentUser()
}
