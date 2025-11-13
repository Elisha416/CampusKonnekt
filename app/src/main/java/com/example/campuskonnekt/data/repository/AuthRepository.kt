package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val user = authResult.user ?: throw Exception("User not found")
            val userModel = firestore.collection("users").document(user.uid).get().await()
                .toObject(User::class.java) ?: throw Exception("User data not found")
            Result.success(userModel)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(email: String, password: String, displayName: String, major: String, year: String): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user ?: throw Exception("User not found")
            val user = User(
                uid = firebaseUser.uid,
                displayName = displayName,
                email = email,
                major = major,
                year = year
            )
            firestore.collection("users").document(firebaseUser.uid).set(user).await()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun logout() {
        auth.signOut()
    }

    fun getCurrentUser(): User? {
        val firebaseUser = auth.currentUser ?: return null
        // This is a simplified version. You might want to fetch the full user profile from Firestore.
        return User(
            uid = firebaseUser.uid,
            displayName = firebaseUser.displayName,
            email = firebaseUser.email,
            major = "", // Placeholder
            year = "" // Placeholder
        )
    }
}