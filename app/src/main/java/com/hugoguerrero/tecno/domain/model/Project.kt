package com.hugoguerrero.tecno.domain.model

data class Project(
    val id: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val categoriaId: String = "",
    val imagenPortada: String? = null,
    val fechaCreado: Long = 0L,
    val creadoPor: String = "",
    val metaFinanciamiento: Double = 0.0,
    val recaudado: Double = 0.0,
    val fechaObjetivo: Long = 0L,
    val visible: Boolean = true,
    val etiquetas: List<String> = emptyList(),
)
