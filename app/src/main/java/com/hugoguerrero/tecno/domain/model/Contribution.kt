package com.hugoguerrero.tecno.domain.model

data class Contribution(
    val id: String = "",
    val proyectoId: String = "",
    val usuarioId: String = "",
    val cantidad: Double = 0.0,
    val fecha: Long = 0L,
    val metodo: String = "paypal" // paypal | google | tarjeta
)
