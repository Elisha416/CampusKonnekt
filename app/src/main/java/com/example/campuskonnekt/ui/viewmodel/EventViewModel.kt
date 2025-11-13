package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.Event
import com.example.campuskonnekt.data.repository.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EventState(
    val isLoading: Boolean = false,
    val events: List<Event> = emptyList(),
    val error: String? = null
)

class EventViewModel(private val eventRepository: EventRepository = EventRepository()) : ViewModel() {
    private val _eventState = MutableStateFlow(EventState())
    val eventState: StateFlow<EventState> = _eventState.asStateFlow()

    init {
        getEvents()
    }

    private fun getEvents() {
        viewModelScope.launch {
            _eventState.update { it.copy(isLoading = true) }
            eventRepository.getEvents().collect { events ->
                _eventState.update { it.copy(isLoading = false, events = events) }
            }
        }
    }

    fun createEvent(
        title: String,
        date: String,
        time: String,
        location: String,
        description: String,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            _eventState.update { it.copy(isLoading = true) }
            val result = eventRepository.createEvent(title, date, time, location, description, imageUrl)
            result.onFailure { e ->
                _eventState.update { it.copy(isLoading = false, error = e.message) }
            }
            // Success is handled by the real-time listener
        }
    }

    fun toggleRsvp(eventId: String) {
        viewModelScope.launch {
            eventRepository.toggleRSVP(eventId)
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            eventRepository.deleteEvent(eventId)
        }
    }
}
