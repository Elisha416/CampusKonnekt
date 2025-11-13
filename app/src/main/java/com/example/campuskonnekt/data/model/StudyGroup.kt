package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class StudyGroup(
    val id: String = "",
    val courseName: String = "",
    val courseCode: String = "",
    val creatorId: String = "",
    val memberIds: List<String> = emptyList(),
    val meetingTime: String = "",
    val location: String = "",
    val description: String = "",
    @ServerTimestamp val timestamp: Date? = null
)
