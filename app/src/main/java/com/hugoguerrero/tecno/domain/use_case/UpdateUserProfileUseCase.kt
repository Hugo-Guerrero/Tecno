package com.hugoguerrero.tecno.domain.use_case

import android.net.Uri
import com.hugoguerrero.tecno.data.repository.UserRepository
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String, displayName: String, bio: String, photoUri: Uri?, paymentDetails: String): Result<Unit> {
        return userRepository.updateUserProfile(uid, displayName, bio, photoUri, paymentDetails)
    }
}
