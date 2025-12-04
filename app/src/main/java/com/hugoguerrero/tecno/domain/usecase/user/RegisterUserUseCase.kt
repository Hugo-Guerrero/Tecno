package com.hugoguerrero.tecno.domain.usecase.user

import com.hugoguerrero.tecno.domain.model.User
import com.hugoguerrero.tecno.domain.repository.UserRepository

class RegisterUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(user: User): Boolean {
        return repository.register(user)
    }
}
