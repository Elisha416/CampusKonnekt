package com.example.campuskonnekt.data.model

data class Service(
    val id: String = "",
    val providerId: String = "",
    val providerName: String = "",
    val title: String = "",
    val category: String = "",
    val price: String = "",
    val description: String = "",
    val rating: Float = 0f,
    val ratingCount: Int = 0,
    val imageUrl: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

data class ServiceRating(
    val id: String = "",
    val serviceId: String = "",
    val userId: String = "",
    val rating: Float = 0f,
    val review: String = "",
    val timestamp: Long = System.currentTimeMillis()
)