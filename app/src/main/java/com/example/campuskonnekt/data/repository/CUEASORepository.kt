package com.example.campuskonnekt.data.repository


import com.example.campuskonnekt.data.model.CUEASOAnnouncement
import com.example.campuskonnekt.data.model.Candidate
import com.example.campuskonnekt.data.model.Petition
import com.example.campuskonnekt.data.model.StudentRepresentative
import com.example.campuskonnekt.data.model.Vote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class CUEASORepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // Announcements
    fun getAnnouncements(): Flow<List<CUEASOAnnouncement>> = callbackFlow {
        val listener = firestore.collection("cueaso_announcements")
            .orderBy("isPinned", Query.Direction.DESCENDING)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val announcements = snapshot?.documents?.mapNotNull {
                    it.toObject(CUEASOAnnouncement::class.java)
                } ?: emptyList()
                trySend(announcements)
            }
        awaitClose { listener.remove() }
    }

    // Voting
    fun getActiveVotes(): Flow<List<Vote>> = callbackFlow {
        val listener = firestore.collection("votes")
            .whereEqualTo("isActive", true)
            .orderBy("startDate", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val votes = snapshot?.documents?.mapNotNull {
                    it.toObject(Vote::class.java)
                } ?: emptyList()
                trySend(votes)
            }
        awaitClose { listener.remove() }
    }

    suspend fun castVote(voteId: String, candidateId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val voteRef = firestore.collection("votes").document(voteId)

            firestore.runTransaction { transaction ->
                val vote = transaction.get(voteRef).toObject(Vote::class.java)
                    ?: throw Exception("Vote not found")

                // Check if user already voted
                if (vote.voterIds.contains(currentUser.uid)) {
                    throw Exception("You have already voted")
                }

                // Check if vote is still active
                val currentTime = System.currentTimeMillis()
                if (currentTime < vote.startDate || currentTime > vote.endDate) {
                    throw Exception("Voting period is not active")
                }

                // Update voter list
                val voters = vote.voterIds.toMutableList()
                voters.add(currentUser.uid)

                // Update candidate vote count
                val candidates = vote.candidates.map { candidate ->
                    if (candidate.id == candidateId) {
                        candidate.copy(voteCount = candidate.voteCount + 1)
                    } else {
                        candidate
                    }
                }

                transaction.update(voteRef, mapOf(
                    "voterIds" to voters,
                    "candidates" to candidates
                ))
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Petitions
    fun getPetitions(): Flow<List<Petition>> = callbackFlow {
        val listener = firestore.collection("petitions")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val petitions = snapshot?.documents?.mapNotNull {
                    it.toObject(Petition::class.java)
                } ?: emptyList()
                trySend(petitions)
            }
        awaitClose { listener.remove() }
    }

    suspend fun createPetition(
        title: String,
        description: String,
        category: String,
        targetSignatures: Int
    ): Result<Petition> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"

            val petitionId = firestore.collection("petitions").document().id
            val petition = Petition(
                id = petitionId,
                title = title,
                description = description,
                category = category,
                creatorId = currentUser.uid,
                creatorName = userName,
                targetSignatures = targetSignatures,
                signatoryIds = listOf(currentUser.uid) // Creator auto-signs
            )

            firestore.collection("petitions").document(petitionId).set(petition).await()
            Result.success(petition)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signPetition(petitionId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val petitionRef = firestore.collection("petitions").document(petitionId)

            firestore.runTransaction { transaction ->
                val petition = transaction.get(petitionRef).toObject(Petition::class.java)
                    ?: throw Exception("Petition not found")

                val signatories = petition.signatoryIds.toMutableList()
                if (signatories.contains(currentUser.uid)) {
                    throw Exception("You have already signed this petition")
                }

                signatories.add(currentUser.uid)
                transaction.update(petitionRef, "signatoryIds", signatories)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Student Representatives
    fun getStudentReps(): Flow<List<StudentRepresentative>> = callbackFlow {
        val listener = firestore.collection("student_representatives")
            .orderBy("position")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val reps = snapshot?.documents?.mapNotNull {
                    it.toObject(StudentRepresentative::class.java)
                } ?: emptyList()
                trySend(reps)
            }
        awaitClose { listener.remove() }
    }
}
