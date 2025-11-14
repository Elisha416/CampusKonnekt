package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Petition(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: String = "",
    val status: String = "Active", // Added status with default value
    val responseFromCUEASO: String = "", // Added response field
    val creatorId: String = "",
    val creatorName: String = "",
    val targetSignatures: Int = 0,
    val signatoryIds: List<String> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
)
