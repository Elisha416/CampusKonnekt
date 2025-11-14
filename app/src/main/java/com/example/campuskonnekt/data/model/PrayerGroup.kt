package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class PrayerGroup(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val meetingTime: String = "",
    val location: String = "",
    val leaderId: String = "",
    val leaderName: String = "",
    val memberIds: List<String> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
)
