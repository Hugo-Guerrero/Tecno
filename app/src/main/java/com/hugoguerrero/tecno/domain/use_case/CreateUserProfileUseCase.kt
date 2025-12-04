package com.hugoguerrero.tecno.domain.use_case

import com.google.firebase.auth.FirebaseUser
import com.hugoguerrero.tecno.data.repository.UserRepository
import com.hugoguerrero.tecno.domain.model.UserType
import javax.inject.Inject

class CreateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: FirebaseUser, name: String, userType: UserType) {
        userRepository.createUserProfile(user, name, userType)
    }
}
