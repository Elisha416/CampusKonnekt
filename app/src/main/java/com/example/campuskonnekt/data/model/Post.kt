package com.example.campuskonnekt.data.model

data class Post(
    val id: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val authorMajor: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val likes: List<String> = emptyList(), // List of user IDs who liked
    val commentCount: Int = 0,
    val timestamp: Long = System.currentTimeMillis()
)

data class Comment(
    val id: String = "",
    val postId: String = "",
    val authorId: String = "",
    val authorName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis()
)