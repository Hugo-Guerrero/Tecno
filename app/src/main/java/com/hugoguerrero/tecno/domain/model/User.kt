package com.hugoguerrero.tecno.domain.model

data class User(
    val id: String = "",
    val nombre: String = "",
    val correo: String = "",
    val fotoUrl: String? = null,
    val fechaRegistrado: Long = 0L,
    val rol: String = "usuario", // usuario | manager | admin
    val creditosDisponibles: Double = 0.0,
    val paymentDetails: String = "" // Nuevo campo para los detalles de pago
)
