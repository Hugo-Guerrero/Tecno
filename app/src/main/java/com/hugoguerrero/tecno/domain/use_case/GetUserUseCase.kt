package com.hugoguerrero.tecno.domain.use_case

import com.hugoguerrero.tecno.data.repository.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: UserRepository
) {
    suspend operator fun invoke(userId: String) = repository.getUserProfile(userId)
}
