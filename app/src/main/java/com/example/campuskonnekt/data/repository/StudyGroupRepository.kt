package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.data.model.StudyGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class StudyGroupRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val groupsCollection = firestore.collection("study_groups")

    // Create study group
    suspend fun createStudyGroup(
        courseName: String,
        courseCode: String,
        meetingTime: String,
        location: String,
        description: String
    ): Result<StudyGroup> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")

            val groupId = groupsCollection.document().id
            val group = StudyGroup(
                id = groupId,
                courseName = courseName,
                courseCode = courseCode,
                creatorId = currentUser.uid,
                memberIds = listOf(currentUser.uid), // Creator is first member
                meetingTime = meetingTime,
                location = location,
                description = description
            )

            groupsCollection.document(groupId).set(group).await()
            Result.success(group)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get study groups
    fun getStudyGroups(): Flow<List<StudyGroup>> = callbackFlow {
        val listener = groupsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val groups = snapshot?.documents?.mapNotNull {
                    it.toObject(StudyGroup::class.java)
                } ?: emptyList()

                trySend(groups)
            }

        awaitClose { listener.remove() }
    }

    // Join/Leave study group
    suspend fun toggleMembership(groupId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val groupRef = groupsCollection.document(groupId)

            firestore.runTransaction { transaction ->
                val group = transaction.get(groupRef).toObject(StudyGroup::class.java)
                    ?: throw Exception("Study group not found")

                val members = group.memberIds.toMutableList()
                if (members.contains(currentUser.uid)) {
                    members.remove(currentUser.uid)
                } else {
                    members.add(currentUser.uid)
                }

                transaction.update(groupRef, "memberIds", members)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete study group
    suspend fun deleteStudyGroup(groupId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val groupDoc = groupsCollection.document(groupId).get().await()
            val group = groupDoc.toObject(StudyGroup::class.java)
                ?: throw Exception("Study group not found")

            if (group.creatorId != currentUser.uid) {
                throw Exception("Unauthorized to delete this group")
            }

            groupsCollection.document(groupId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}