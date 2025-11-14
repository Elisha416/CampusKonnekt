package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.*
import com.example.campuskonnekt.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch


data class CUEASOState(
    val announcements: List<CUEASOAnnouncement> = emptyList(),
    val votes: List<Vote> = emptyList(),
    val petitions: List<Petition> = emptyList(),
    val studentReps: List<StudentRepresentative> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class CUEASOViewModel : ViewModel() {
    private val repository = CUEASORepository()

    private val _cueasoState = MutableStateFlow(CUEASOState())
    val cueasoState: StateFlow<CUEASOState> = _cueasoState

    init {
        loadAnnouncements()
        loadVotes()
        loadPetitions()
        loadStudentReps()
    }

    private fun loadAnnouncements() {
        viewModelScope.launch {
            repository.getAnnouncements()
                .catch { e ->
                    _cueasoState.value = _cueasoState.value.copy(error = e.message)
                }
                .collect { announcements ->
                    _cueasoState.value = _cueasoState.value.copy(announcements = announcements)
                }
        }
    }

    private fun loadVotes() {
        viewModelScope.launch {
            repository.getActiveVotes()
                .catch { e ->
                    _cueasoState.value = _cueasoState.value.copy(error = e.message)
                }
                .collect { votes ->
                    _cueasoState.value = _cueasoState.value.copy(votes = votes)
                }
        }
    }

    private fun loadPetitions() {
        viewModelScope.launch {
            repository.getPetitions()
                .catch { e ->
                    _cueasoState.value = _cueasoState.value.copy(error = e.message)
                }
                .collect { petitions ->
                    _cueasoState.value = _cueasoState.value.copy(petitions = petitions)
                }
        }
    }

    private fun loadStudentReps() {
        viewModelScope.launch {
            repository.getStudentReps()
                .catch { e ->
                    _cueasoState.value = _cueasoState.value.copy(error = e.message)
                }
                .collect { reps ->
                    _cueasoState.value = _cueasoState.value.copy(studentReps = reps)
                }
        }
    }

    fun castVote(voteId: String, candidateId: String) {
        viewModelScope.launch {
            _cueasoState.value = _cueasoState.value.copy(isLoading = true)

            repository.castVote(voteId, candidateId).fold(
                onSuccess = {
                    _cueasoState.value = _cueasoState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _cueasoState.value = _cueasoState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun createPetition(
        title: String,
        description: String,
        category: String,
        targetSignatures: Int
    ) {
        viewModelScope.launch {
            _cueasoState.value = _cueasoState.value.copy(isLoading = true)

            repository.createPetition(title, description, category, targetSignatures).fold(
                onSuccess = {
                    _cueasoState.value = _cueasoState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _cueasoState.value = _cueasoState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun signPetition(petitionId: String) {
        viewModelScope.launch {
            repository.signPetition(petitionId).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _cueasoState.value = _cueasoState.value.copy(error = exception.message)
                }
            )
        }
    }
}