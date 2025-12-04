package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.UserRepository
import javax.inject.Inject

class UpdateFcmTokenUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, token: String) {
        try {
            userRepository.updateFcmToken(userId, token)
        } catch (e: Exception) {
            // Manejar el error, por ejemplo, registrarlo
            e.printStackTrace()
        }
    }
}
