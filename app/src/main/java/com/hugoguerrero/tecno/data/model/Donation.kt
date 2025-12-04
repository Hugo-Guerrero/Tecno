package com.hugoguerrero.tecno.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

enum class DonationStatus {
    PENDING,
    COMPLETED,
    FAILED
}

data class Donation(
    val id: String = "",
    val projectId: String = "",
    val donorUid: String = "",
    val amount: Double = 0.0,
    val message: String = "",
    val status: DonationStatus = DonationStatus.PENDING,
    val proofImageUrl: String = "", // Nuevo campo para la URL del comprobante
    @ServerTimestamp
    val createdAt: Date? = null
)
