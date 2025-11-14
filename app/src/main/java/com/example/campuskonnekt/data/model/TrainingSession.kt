package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class TrainingSession(
    val id: String = "",
    val title: String = "",
    val sport: String = "",
    val day: String = "",
    val time: String = "",
    val venue: String = "",
    val coach: String = "",
    val description: String = "",
    val isOpenToAll: Boolean = false,
    @ServerTimestamp val timestamp: Date? = null
)
