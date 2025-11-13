package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class ProfileState(
    val user: User? = null,
    val postCount: Int = 0,
    val eventCount: Int = 0,
    val groupCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)

            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")

                // Get user data
                val userDoc = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                val user = userDoc.toObject(User::class.java)

                // Get post count
                val postCount = firestore.collection("posts")
                    .whereEqualTo("authorId", currentUser.uid)
                    .get()
                    .await()
                    .size()

                // Get event count (events user is attending)
                val eventCount = firestore.collection("events")
                    .whereArrayContains("attendeeIds", currentUser.uid)
                    .get()
                    .await()
                    .size()

                // Get group count (groups user is member of)
                val groupCount = firestore.collection("study_groups")
                    .whereArrayContains("memberIds", currentUser.uid)
                    .get()
                    .await()
                    .size()

                _profileState.value = ProfileState(
                    user = user,
                    postCount = postCount,
                    eventCount = eventCount,
                    groupCount = groupCount
                )
            } catch (e: Exception) {
                _profileState.value = ProfileState(error = e.message)
            }
        }
    }

    fun updateProfile(
        displayName: String,
        major: String,
        year: String,
        bio: String,
        interests: List<String>
    ) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(isLoading = true)

            try {
                val currentUser = auth.currentUser ?: throw Exception("User not logged in")

                val updates = mapOf(
                    "displayName" to displayName,
                    "major" to major,
                    "year" to year,
                    "bio" to bio,
                    "interests" to interests
                )

                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(updates)
                    .await()

                loadProfile() // Reload to get updated data
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}