package com.example.campuskonnekt.data.repository


import com.example.campuskonnekt.data.model.MassSchedule
import com.example.campuskonnekt.data.model.PrayerGroup
import com.example.campuskonnekt.data.model.ReligiousEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ChapelRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Mass Schedules
    fun getMassSchedules(): Flow<List<MassSchedule>> = callbackFlow {
        val listener = firestore.collection("mass_schedules")
            .orderBy("time")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val schedules = snapshot?.documents?.mapNotNull {
                    it.toObject(MassSchedule::class.java)
                } ?: emptyList()
                trySend(schedules)
            }
        awaitClose { listener.remove() }
    }

    // Religious Events
    fun getReligiousEvents(): Flow<List<ReligiousEvent>> = callbackFlow {
        val listener = firestore.collection("religious_events")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val events = snapshot?.documents?.mapNotNull {
                    it.toObject(ReligiousEvent::class.java)
                } ?: emptyList()
                trySend(events)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createReligiousEvent(
        title: String,
        description: String,
        date: String,
        time: String,
        location: String,
        eventType: String,
        maxParticipants: Int
    ): Result<ReligiousEvent> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"

            val eventId = firestore.collection("religious_events").document().id
            val event = ReligiousEvent(
                id = eventId,
                title = title,
                description = description,
                date = date,
                time = time,
                location = location,
                eventType = eventType,
                organizerId = currentUser.uid,
                organizerName = userName,
                maxParticipants = maxParticipants
            )

            firestore.collection("religious_events").document(eventId).set(event).await()
            Result.success(event)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleEventParticipation(eventId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val eventRef = firestore.collection("religious_events").document(eventId)

            firestore.runTransaction { transaction ->
                val event = transaction.get(eventRef).toObject(ReligiousEvent::class.java)
                    ?: throw Exception("Event not found")

                val participants = event.participantIds.toMutableList()
                if (participants.contains(currentUser.uid)) {
                    participants.remove(currentUser.uid)
                } else {
                    if (event.maxParticipants > 0 && participants.size >= event.maxParticipants) {
                        throw Exception("Event is full")
                    }
                    participants.add(currentUser.uid)
                }

                transaction.update(eventRef, "participantIds", participants)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Prayer Groups
    fun getPrayerGroups(): Flow<List<PrayerGroup>> = callbackFlow {
        val listener = firestore.collection("prayer_groups")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val groups = snapshot?.documents?.mapNotNull {
                    it.toObject(PrayerGroup::class.java)
                } ?: emptyList()
                trySend(groups)
            }
        awaitClose { listener.remove() }
    }

    suspend fun joinPrayerGroup(groupId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val groupRef = firestore.collection("prayer_groups").document(groupId)

            firestore.runTransaction { transaction ->
                val group = transaction.get(groupRef).toObject(PrayerGroup::class.java)
                    ?: throw Exception("Group not found")

                val members = group.memberIds.toMutableList()
                if (!members.contains(currentUser.uid)) {
                    members.add(currentUser.uid)
                }

                transaction.update(groupRef, "memberIds", members)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}