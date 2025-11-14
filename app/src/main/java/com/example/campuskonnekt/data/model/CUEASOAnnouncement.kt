package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class CUEASOAnnouncement(
    val id: String = "",
    val title: String = "",
    val content: String = "", // Renamed from message
    val category: String = "General", // Added category
    val authorName: String = "CUEASO Office", // Added author name
    val authorPosition: String = "Student Government", // Added author position
    val isPinned: Boolean = false,
    @ServerTimestamp
    val timestamp: Date? = null
)
