package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.campuskonnekt.data.model.Event
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

data class EventsState(
    val events: List<Event> = emptyList(),
    val isLoading: Boolean = true
)

class EventsViewModel : ViewModel() {

    private val _eventsState = MutableStateFlow(EventsState())
    val eventsState: StateFlow<EventsState> = _eventsState

    init {
        // TODO: Load events from a repository
        // For now, we'll use sample data
        val sampleEvents = listOf(
            Event(id = "1", title = "Campus Hackathon", organizerName = "Dev Club", date = "Dec 12, 2023", time = "10:00 AM", location = "Auditorium", description = "A fun hackathon for all students.", attendeeIds = listOf("a", "b")),
            Event(id = "2", title = "Winter Fest", organizerName = "Student Council", date = "Dec 20, 2023", time = "5:00 PM", location = "Main Quad", description = "Enjoy the winter season with us!", attendeeIds = listOf("c", "d", "e"))
        )
        _eventsState.value = EventsState(events = sampleEvents, isLoading = false)
    }

    fun toggleRSVP(eventId: String) {
        // TODO: Implement RSVP logic
    }

    fun createEvent(title: String, date: String, time: String, location: String, description: String) {
        // TODO: Implement event creation logic
    }
}
