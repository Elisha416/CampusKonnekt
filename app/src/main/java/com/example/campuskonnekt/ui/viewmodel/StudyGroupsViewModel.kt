package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.StudyGroup
import com.example.campuskonnekt.data.repository.StudyGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class StudyGroupsState(
    val groups: List<StudyGroup> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class StudyGroupsViewModel : ViewModel() {
    private val repository = StudyGroupRepository()

    private val _groupsState = MutableStateFlow(StudyGroupsState())
    val groupsState: StateFlow<StudyGroupsState> = _groupsState

    init {
        loadStudyGroups()
    }

    private fun loadStudyGroups() {
        viewModelScope.launch {
            _groupsState.value = _groupsState.value.copy(isLoading = true)

            repository.getStudyGroups()
                .catch { e ->
                    _groupsState.value = StudyGroupsState(error = e.message)
                }
                .collect { groups ->
                    _groupsState.value = StudyGroupsState(groups = groups)
                }
        }
    }

    fun createStudyGroup(
        courseName: String,
        courseCode: String,
        meetingTime: String,
        location: String,
        description: String
    ) {
        viewModelScope.launch {
            _groupsState.value = _groupsState.value.copy(isLoading = true)

            repository.createStudyGroup(
                courseName, courseCode, meetingTime, location, description
            ).fold(
                onSuccess = {
                    _groupsState.value = _groupsState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _groupsState.value = _groupsState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun toggleMembership(groupId: String) {
        viewModelScope.launch {
            repository.toggleMembership(groupId).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _groupsState.value = _groupsState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun deleteStudyGroup(groupId: String) {
        viewModelScope.launch {
            repository.deleteStudyGroup(groupId).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _groupsState.value = _groupsState.value.copy(error = exception.message)
                }
            )
        }
    }
}