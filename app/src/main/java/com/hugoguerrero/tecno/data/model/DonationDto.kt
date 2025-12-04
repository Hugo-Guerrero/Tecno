package com.hugoguerrero.tecno.data.model

data class DonationDto(
    val id: String? = null,
    val projectId: String? = null,
    val userId: String? = null,
    val amount: Double? = null,
    val paymentId: String? = null,
    val createdAt: Long? = null
)
