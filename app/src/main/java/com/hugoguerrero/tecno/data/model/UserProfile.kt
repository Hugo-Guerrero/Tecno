package com.hugoguerrero.tecno.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import com.hugoguerrero.tecno.domain.model.UserType
import java.util.Date

@IgnoreExtraProperties // Ignora campos desconocidos al leer de Firestore
data class UserProfile(
    val uid: String = "",
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val bio: String = "",
    val university: String = "",
    val userType: UserType = UserType.STUDENT,
    val fcmToken: String = "",
    val paymentDetails: String = "",
    @ServerTimestamp
    val createdAt: Date = Date()
)
