package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class TeamRoster(
    val playerId: String = "",
    val playerName: String = "",
    val sport: String = "",
    val jerseyNumber: Int = 0,
    val position: String = "",
    val course: String = "",
    val year: String = "",
    val imageUrl: String = "",
    val stats: PlayerStats = PlayerStats()
)

@IgnoreExtraProperties
data class PlayerStats(
    val matchesPlayed: Int = 0,
    val tries: Int = 0,
    val goals: Int = 0,
    val points: Int = 0
)
