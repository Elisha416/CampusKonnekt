package com.example.campuskonnekt.ui.viewmodel.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.StudyGroup
import com.example.campuskonnekt.data.repository.StudyGroupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class StudyGroupState(
    val isLoading: Boolean = false,
    val studyGroups: List<StudyGroup> = emptyList(),
    val error: String? = null
)

class StudyGroupViewModel(private val studyGroupRepository: StudyGroupRepository = StudyGroupRepository()) : ViewModel() {
    private val _studyGroupState = MutableStateFlow(StudyGroupState())
    val studyGroupState: StateFlow<StudyGroupState> = _studyGroupState.asStateFlow()

    init {
        getStudyGroups()
    }

    private fun getStudyGroups() {
        viewModelScope.launch {
            _studyGroupState.update { it.copy(isLoading = true) }
            studyGroupRepository.getStudyGroups().collect { studyGroups ->
                _studyGroupState.update { it.copy(isLoading = false, studyGroups = studyGroups) }
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
            _studyGroupState.update { it.copy(isLoading = true) }
            val result = studyGroupRepository.createStudyGroup(courseName, courseCode, meetingTime, location, description)
            result.onFailure { e ->
                _studyGroupState.update { it.copy(isLoading = false, error = e.message) }
            }
            // Success is handled by the real-time listener
        }
    }

    fun toggleMembership(groupId: String) {
        viewModelScope.launch {
            studyGroupRepository.toggleMembership(groupId)
        }
    }

    fun deleteStudyGroup(groupId: String) {
        viewModelScope.launch {
            studyGroupRepository.deleteStudyGroup(groupId)
        }
    }
}
