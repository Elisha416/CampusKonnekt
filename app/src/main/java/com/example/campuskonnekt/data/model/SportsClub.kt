package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class SportsClub(
    val id: String = "",
    val name: String = "",
    val sport: String = "",
    val description: String = "",
    val presidentName: String = "",
    val achievements: List<String> = emptyList(),
    val trainingSchedule: String = "",
    val membershipFee: String = "",
    val memberIds: List<String> = emptyList(),
    @ServerTimestamp val timestamp: Date? = null
)
