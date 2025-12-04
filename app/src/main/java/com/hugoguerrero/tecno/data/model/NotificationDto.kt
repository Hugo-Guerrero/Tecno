package com.hugoguerrero.tecno.data.model

data class NotificationDto(
    val id: String? = null,
    val userId: String? = null,          // A quién va dirigida
    val title: String? = null,
    val message: String? = null,
    val type: String? = null,            // "donation", "project_approved", etc.
    val projectId: String? = null,       // opcional si aplica
    val isRead: Boolean = false,
    val createdAt: Long? = null
)
