package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class ReligiousEvent(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val eventType: String = "",
    val organizerId: String = "",
    val organizerName: String = "",
    val maxParticipants: Int = 0,
    val participantIds: List<String> = emptyList(),
    @ServerTimestamp
    val timestamp: Date? = null
)
