package com.example.campuskonnekt.data.model

/**
 * Represents the overall state for the Chapel screen, containing lists of schedules, events, and groups.
 */
data class Chapel(
    val massSchedules: List<MassSchedule> = emptyList(),
    val religiousEvents: List<ReligiousEvent> = emptyList(),
    val prayerGroups: List<PrayerGroup> = emptyList()
)
