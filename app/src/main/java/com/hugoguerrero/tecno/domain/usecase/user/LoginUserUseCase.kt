package com.hugoguerrero.tecno.domain.usecase.user

import com.hugoguerrero.tecno.domain.model.User
import com.hugoguerrero.tecno.domain.repository.UserRepository

class LoginUserUseCase(
    private val repository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User? {
        return repository.login(email, password)
    }
}
