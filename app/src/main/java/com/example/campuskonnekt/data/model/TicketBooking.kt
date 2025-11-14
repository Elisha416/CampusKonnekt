package com.example.campuskonnekt.data.model

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class TicketBooking(
    val id: String = "",
    val matchId: String = "",
    val userId: String = "",
    val userName: String = "",
    val ticketType: String = "",
    val quantity: Int = 0,
    val totalPrice: String = "",
    val qrCode: String = "",
    @ServerTimestamp val timestamp: Date? = null
)
