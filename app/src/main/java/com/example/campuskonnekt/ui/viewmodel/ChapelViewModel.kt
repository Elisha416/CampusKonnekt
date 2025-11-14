package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.*
import com.example.campuskonnekt.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class ChapelState(
    val massSchedules: List<MassSchedule> = emptyList(),
    val religiousEvents: List<ReligiousEvent> = emptyList(),
    val prayerGroups: List<PrayerGroup> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class ChapelViewModel : ViewModel() {
    private val repository = ChapelRepository()

    private val _chapelState = MutableStateFlow(ChapelState())
    val chapelState: StateFlow<ChapelState> = _chapelState

    init {
        loadMassSchedules()
        loadReligiousEvents()
        loadPrayerGroups()
    }

    private fun loadMassSchedules() {
        viewModelScope.launch {
            repository.getMassSchedules()
                .catch { e ->
                    _chapelState.value = _chapelState.value.copy(error = e.message)
                }
                .collect { schedules ->
                    _chapelState.value = _chapelState.value.copy(massSchedules = schedules)
                }
        }
    }

    private fun loadReligiousEvents() {
        viewModelScope.launch {
            repository.getReligiousEvents()
                .catch { e ->
                    _chapelState.value = _chapelState.value.copy(error = e.message)
                }
                .collect { events ->
                    _chapelState.value = _chapelState.value.copy(religiousEvents = events)
                }
        }
    }

    private fun loadPrayerGroups() {
        viewModelScope.launch {
            repository.getPrayerGroups()
                .catch { e ->
                    _chapelState.value = _chapelState.value.copy(error = e.message)
                }
                .collect { groups ->
                    _chapelState.value = _chapelState.value.copy(prayerGroups = groups)
                }
        }
    }

    fun createReligiousEvent(
        title: String,
        description: String,
        date: String,
        time: String,
        location: String,
        eventType: String,
        maxParticipants: Int
    ) {
        viewModelScope.launch {
            _chapelState.value = _chapelState.value.copy(isLoading = true)

            repository.createReligiousEvent(
                title, description, date, time, location, eventType, maxParticipants
            ).fold(
                onSuccess = {
                    _chapelState.value = _chapelState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _chapelState.value = _chapelState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun toggleEventParticipation(eventId: String) {
        viewModelScope.launch {
            repository.toggleEventParticipation(eventId).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _chapelState.value = _chapelState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun joinPrayerGroup(groupId: String) {
        viewModelScope.launch {
            repository.joinPrayerGroup(groupId).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _chapelState.value = _chapelState.value.copy(error = exception.message)
                }
            )
        }
    }
}