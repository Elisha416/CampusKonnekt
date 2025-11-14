package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class MassSchedule(
    val id: String = "",
    val title: String = "",
    val dayOfWeek: String = "",
    val time: String = "",
    val location: String = "",
    val language: String = "",
    val priestName: String = "",
    @ServerTimestamp val timestamp: Date? = null
)
