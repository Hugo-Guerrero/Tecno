package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.AuthResultDto
import com.hugoguerrero.tecno.data.model.RegisterRequestDto
import com.hugoguerrero.tecno.data.model.UserDto

interface UserRemoteDataSource {
    suspend fun login(email: String, password: String): AuthResultDto?
    suspend fun register(request: RegisterRequestDto): AuthResultDto?
    suspend fun getUser(userId: String): UserDto?
    suspend fun updateUser(user: UserDto): Boolean
}