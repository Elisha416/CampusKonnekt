package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.data.model.Event
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class EventRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val eventsCollection = firestore.collection("events")

    // Create event
    suspend fun createEvent(
        title: String,
        date: String,
        time: String,
        location: String,
        description: String,
        imageUrl: String? = null
    ): Result<Event> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"

            val eventId = eventsCollection.document().id
            val event = Event(
                id = eventId,
                title = title,
                organizerId = currentUser.uid,
                organizerName = userName,
                date = date,
                time = time,
                location = location,
                description = description,
                imageUrl = imageUrl
            )

            eventsCollection.document(eventId).set(event).await()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get events
    fun getEvents(): Flow<List<Event>> = callbackFlow {
        val listener = eventsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val events = snapshot?.documents?.mapNotNull {
                    it.toObject(Event::class.java)
                } ?: emptyList()

                trySend(events)
            }

        awaitClose { listener.remove() }
    }

    // RSVP to event
    suspend fun toggleRSVP(eventId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val eventRef = eventsCollection.document(eventId)

            firestore.runTransaction { transaction ->
                val event = transaction.get(eventRef).toObject(Event::class.java)
                    ?: throw Exception("Event not found")

                val attendees = event.attendeeIds.toMutableList()
                if (attendees.contains(currentUser.uid)) {
                    attendees.remove(currentUser.uid)
                } else {
                    attendees.add(currentUser.uid)
                }

                transaction.update(eventRef, "attendeeIds", attendees)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete event
    suspend fun deleteEvent(eventId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val eventDoc = eventsCollection.document(eventId).get().await()
            val event = eventDoc.toObject(Event::class.java) ?: throw Exception("Event not found")

            if (event.organizerId != currentUser.uid) {
                throw Exception("Unauthorized to delete this event")
            }

            eventsCollection.document(eventId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
