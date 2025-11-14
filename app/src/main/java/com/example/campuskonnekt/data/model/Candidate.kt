package com.example.campuskonnekt.data.model


data class Candidate(
    val id: String = "",
    val name: String = "",
    val bio: String = "",
    val course: String = "", // Added course
    val year: String = "", // Added year
    val photoUrl: String = "",
    val voteCount: Int = 0
)
