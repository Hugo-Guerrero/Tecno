package com.hugoguerrero.tecno.data.model

data class PaymentDto(
    val id: String? = null,
    val userId: String? = null,
    val projectId: String? = null,
    val amount: Double? = null,
    val method: String? = null,
    val transactionId: String? = null,
    val status: String? = null,
    val createdAt: Long? = null
)
