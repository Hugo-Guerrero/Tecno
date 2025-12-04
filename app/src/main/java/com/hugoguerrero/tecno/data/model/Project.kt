package com.hugoguerrero.tecno.data.model

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

@IgnoreExtraProperties // Anotación para ignorar campos desconocidos
data class Project(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val ownerUid: String = "",
    val category: String = "",
    val institution: String = "",
    val fundingGoal: Double = 0.0,
    val currentFunding: Double = 0.0,
    val imageUrl: String = "",
    val documentUrls: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    @ServerTimestamp
    val createdAt: Date? = null
) {
    @get:Exclude
    val progress: Float
        get() = if (fundingGoal > 0) ((currentFunding / fundingGoal) * 100).toFloat() else 0f
}
