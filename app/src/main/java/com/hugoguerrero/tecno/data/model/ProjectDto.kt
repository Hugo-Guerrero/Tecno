package com.hugoguerrero.tecno.data.model

data class ProjectDto(
    val id: String? = null,
    val ownerId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val category: String? = null,
    val imageUrl: String? = null,
    val goal: Double? = null,
    val currentAmount: Double? = null,
    val createdAt: Long? = null
)
