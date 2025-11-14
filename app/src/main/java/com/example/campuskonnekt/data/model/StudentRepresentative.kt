package com.example.campuskonnekt.data.model

data class StudentRepresentative(
    val id: String = "",
    val name: String = "",
    val position: String = "",
    val faculty: String = "", // Added faculty
    val bio: String = "", // Added bio
    val email: String = "",
    val phone: String = "",
    val officeLocation: String = "", // Added office location
    val officeHours: String = "", // Added office hours
    val photoUrl: String = ""
)
