package com.hugoguerrero.tecno.domain.usecase.user

import com.hugoguerrero.tecno.domain.model.User
import com.hugoguerrero.tecno.domain.repository.UserRepository

class GetUserByIdUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(id: Int): User? {
        return repository.getUserById(id)
    }
}
