package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Vote(
    val id: String = "",
    val title: String = "",
    val description: String = "", // Added description
    val category: String = "General Election", // Added category
    val candidates: List<Candidate> = emptyList(),
    val startDate: Long = 0,
    val endDate: Long = 0,
    val isActive: Boolean = false,
    val voterIds: List<String> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
)
