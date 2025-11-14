package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.data.model.Match
import com.example.campuskonnekt.data.model.SportsClub
import com.example.campuskonnekt.data.model.TeamRoster
import com.example.campuskonnekt.data.model.TicketBooking
import com.example.campuskonnekt.data.model.TrainingSession
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SportsRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Matches
    fun getMatches(): Flow<List<Match>> = callbackFlow {
        val listener = firestore.collection("matches")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val matches = snapshot?.documents?.mapNotNull {
                    it.toObject(Match::class.java)
                } ?: emptyList()
                trySend(matches)
            }
        awaitClose { listener.remove() }
    }

    // Team Roster
    fun getTeamRoster(sport: String): Flow<List<TeamRoster>> = callbackFlow {
        val listener = firestore.collection("team_roster")
            .whereEqualTo("sport", sport)
            .orderBy("jerseyNumber")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val roster = snapshot?.documents?.mapNotNull {
                    it.toObject(TeamRoster::class.java)
                } ?: emptyList()
                trySend(roster)
            }
        awaitClose { listener.remove() }
    }

    // Training Sessions
    fun getTrainingSessions(): Flow<List<TrainingSession>> = callbackFlow {
        val listener = firestore.collection("training_sessions")
            .orderBy("day")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val sessions = snapshot?.documents?.mapNotNull {
                    it.toObject(TrainingSession::class.java)
                } ?: emptyList()
                trySend(sessions)
            }
        awaitClose { listener.remove() }
    }

    // Sports Clubs
    fun getSportsClubs(): Flow<List<SportsClub>> = callbackFlow {
        val listener = firestore.collection("sports_clubs")
            .orderBy("name")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val clubs = snapshot?.documents?.mapNotNull {
                    it.toObject(SportsClub::class.java)
                } ?: emptyList()
                trySend(clubs)
            }
        awaitClose { listener.remove() }
    }

    suspend fun joinSportsClub(clubId: String): Result<Unit> = runCatching {
        val currentUser = auth.currentUser ?: throw Exception("User not logged in")
        val clubRef = firestore.collection("sports_clubs").document(clubId)

        firestore.runTransaction { transaction ->
            val club = transaction.get(clubRef).toObject(SportsClub::class.java)
                ?: throw Exception("Club not found")

            // Safely handle if memberIds is null
            val members = club.memberIds?.toMutableList() ?: mutableListOf()
            if (!members.contains(currentUser.uid)) {
                members.add(currentUser.uid)
            }

            transaction.update(clubRef, "memberIds", members)
        }.await()
        // runCatching will return Result.success(Unit) automatically
    }

    // Ticket Booking
    suspend fun bookTicket(
        matchId: String,
        ticketType: String,
        quantity: Int,
        totalPrice: String
    ): Result<TicketBooking> = runCatching {
        val currentUser = auth.currentUser ?: throw Exception("User not logged in")
        val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
        val userName = userDoc.getString("displayName") ?: "Unknown"

        val bookingId = firestore.collection("ticket_bookings").document().id
        val booking = TicketBooking(
            id = bookingId,
            matchId = matchId,
            userId = currentUser.uid,
            userName = userName,
            ticketType = ticketType,
            quantity = quantity,
            totalPrice = totalPrice,
            qrCode = generateQRCode(bookingId)
        )

        firestore.collection("ticket_bookings").document(bookingId).set(booking).await()
        booking // This value is automatically wrapped in Result.success
    }

    private fun generateQRCode(bookingId: String): String {
        // Simple QR code generation - you can enhance this
        return "CUEA-TICKET-$bookingId"
    }
}
