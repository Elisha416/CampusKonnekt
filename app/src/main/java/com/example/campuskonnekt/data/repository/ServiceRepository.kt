package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.data.model.Service
import com.example.campuskonnekt.data.model.ServiceRating
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ServiceRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val servicesCollection = firestore.collection("services")
    private val ratingsCollection = firestore.collection("service_ratings")

    // Create service
    suspend fun createService(
        title: String,
        category: String,
        price: String,
        description: String,
        imageUrl: String? = null
    ): Result<Service> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"

            val serviceId = servicesCollection.document().id
            val service = Service(
                id = serviceId,
                providerId = currentUser.uid,
                providerName = userName,
                title = title,
                category = category,
                price = price,
                description = description,
                imageUrl = imageUrl
            )

            servicesCollection.document(serviceId).set(service).await()
            Result.success(service)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get services
    fun getServices(): Flow<List<Service>> = callbackFlow {
        val listener = servicesCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val services = snapshot?.documents?.mapNotNull {
                    it.toObject(Service::class.java)
                } ?: emptyList()

                trySend(services)
            }

        awaitClose { listener.remove() }
    }

    // Get services by category
    fun getServicesByCategory(category: String): Flow<List<Service>> = callbackFlow {
        val listener = servicesCollection
            .whereEqualTo("category", category)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val services = snapshot?.documents?.mapNotNull {
                    it.toObject(Service::class.java)
                } ?: emptyList()

                trySend(services)
            }

        awaitClose { listener.remove() }
    }

    // Rate service
    suspend fun rateService(serviceId: String, rating: Float, review: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")

            // Add rating
            val ratingId = ratingsCollection.document().id
            val serviceRating = ServiceRating(
                id = ratingId,
                serviceId = serviceId,
                userId = currentUser.uid,
                rating = rating,
                review = review
            )

            ratingsCollection.document(ratingId).set(serviceRating).await()

            // Update service average rating
            val ratings = ratingsCollection
                .whereEqualTo("serviceId", serviceId)
                .get()
                .await()
                .documents
                .mapNotNull { it.toObject(ServiceRating::class.java) }

            val avgRating = ratings.map { it.rating }.average().toFloat()
            val ratingCount = ratings.size

            servicesCollection.document(serviceId).update(
                mapOf(
                    "rating" to avgRating,
                    "ratingCount" to ratingCount
                )
            ).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete service
    suspend fun deleteService(serviceId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val serviceDoc = servicesCollection.document(serviceId).get().await()
            val service = serviceDoc.toObject(Service::class.java)
                ?: throw Exception("Service not found")

            if (service.providerId != currentUser.uid) {
                throw Exception("Unauthorized to delete this service")
            }

            servicesCollection.document(serviceId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}