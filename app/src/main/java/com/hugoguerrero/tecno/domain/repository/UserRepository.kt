package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.domain.model.User

interface UserRepository {
    suspend fun login(email: String, password: String): User?
    suspend fun register(user: User): Boolean
    suspend fun logout(): Boolean
    suspend fun getUserById(id: Int): User?
    suspend fun updateUser(user: User): Boolean
}
