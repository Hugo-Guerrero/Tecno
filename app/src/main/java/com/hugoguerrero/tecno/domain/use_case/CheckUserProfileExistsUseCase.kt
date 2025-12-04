package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.UserRepository
import javax.inject.Inject

class CheckUserProfileExistsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String): Boolean {
        return userRepository.userProfileExists(uid)
    }
}
