package com.hugoguerrero.tecno.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Complaint(
    val id: String = "",
    val projectId: String = "",
    val reporterUid: String = "",
    val reason: String = "",
    val description: String = "",
    @ServerTimestamp
    val createdAt: Date? = null
)
