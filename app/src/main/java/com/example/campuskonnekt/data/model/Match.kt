package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Match(
    val id: String = "",
    val sport: String = "",
    val status: String = "Upcoming",
    val homeTeam: String = "",
    val awayTeam: String = "",
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val date: String = "",
    val time: String = "",
    val venue: String = "",
    val competition: String = "",
    val ticketPrice: String = "",
    @ServerTimestamp val timestamp: Date? = null
)
