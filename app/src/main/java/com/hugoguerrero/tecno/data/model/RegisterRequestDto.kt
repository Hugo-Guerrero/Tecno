package com.hugoguerrero.tecno.data.model

data class RegisterRequestDto(
    val email: String,
    val password: String,
    val name: String,
    val lastName: String,
    val phone: String
)
