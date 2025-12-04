package com.hugoguerrero.tecno.data.remote

import com.hugoguerrero.tecno.data.model.AuthResultDto
import com.hugoguerrero.tecno.data.model.LoginRequestDto
import com.hugoguerrero.tecno.data.model.RegisterRequestDto

interface AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): AuthResultDto
    suspend fun register(request: RegisterRequestDto): AuthResultDto
    suspend fun loginWithGoogle(idToken: String): AuthResultDto
    suspend fun logout()
    fun getCurrentUserId(): String?
}