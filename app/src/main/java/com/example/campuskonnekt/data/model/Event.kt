package com.example.campuskonnekt.data.model

data class Event(
    val id: String = "",
    val title: String = "",
    val organizerId: String = "",
    val organizerName: String = "",
    val date: String = "",
    val time: String = "",
    val location: String = "",
    val description: String = "",
    val attendeeIds: List<String> = emptyList(),
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)