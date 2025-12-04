package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.model.UserProfile
import com.hugoguerrero.tecno.data.repository.UserRepository
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String): UserProfile? {
        return userRepository.getUserProfile(uid)
    }
}
