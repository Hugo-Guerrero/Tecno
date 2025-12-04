package com.hugoguerrero.tecno.domain.model

data class Document(
    val id: String = "",
    val proyectoId: String = "",
    val url: String = "",
    val nombre: String = "",
    val tipo: String = "", // pdf, img, zip
)
