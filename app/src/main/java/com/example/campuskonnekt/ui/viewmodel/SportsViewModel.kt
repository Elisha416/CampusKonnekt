package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.*
import com.example.campuskonnekt.data.repository.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class SportsState(
    val matches: List<Match> = emptyList(),
    val selectedSport: String = "Rugby",
    val teamRoster: List<TeamRoster> = emptyList(),
    val trainingSessions: List<TrainingSession> = emptyList(),
    val sportsClubs: List<SportsClub> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class SportsViewModel : ViewModel() {
    private val repository = SportsRepository()

    private val _sportsState = MutableStateFlow(SportsState())
    val sportsState: StateFlow<SportsState> = _sportsState

    init {
        loadMatches()
        loadTeamRoster("Rugby")
        loadTrainingSessions()
        loadSportsClubs()
    }

    private fun loadMatches() {
        viewModelScope.launch {
            repository.getMatches()
                .catch { e ->
                    _sportsState.value = _sportsState.value.copy(error = e.message)
                }
                .collect { matches ->
                    _sportsState.value = _sportsState.value.copy(matches = matches)
                }
        }
    }

    private fun loadTeamRoster(sport: String) {
        viewModelScope.launch {
            repository.getTeamRoster(sport)
                .catch { e ->
                    _sportsState.value = _sportsState.value.copy(error = e.message)
                }
                .collect { roster ->
                    _sportsState.value = _sportsState.value.copy(teamRoster = roster)
                }
        }
    }

    private fun loadTrainingSessions() {
        viewModelScope.launch {
            repository.getTrainingSessions()
                .catch { e ->
                    _sportsState.value = _sportsState.value.copy(error = e.message)
                }
                .collect { sessions ->
                    _sportsState.value = _sportsState.value.copy(trainingSessions = sessions)
                }
        }
    }

    private fun loadSportsClubs() {
        viewModelScope.launch {
            repository.getSportsClubs()
                .catch { e ->
                    _sportsState.value = _sportsState.value.copy(error = e.message)
                }
                .collect { clubs ->
                    _sportsState.value = _sportsState.value.copy(sportsClubs = clubs)
                }
        }
    }

    fun setSport(sport: String) {
        _sportsState.value = _sportsState.value.copy(selectedSport = sport)
        loadTeamRoster(sport)
    }

    fun joinSportsClub(clubId: String) {
        viewModelScope.launch {
            repository.joinSportsClub(clubId).fold(
                onSuccess = { },
                onFailure = { exception ->
                    _sportsState.value = _sportsState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun bookTicket(
        matchId: String,
        ticketType: String,
        quantity: Int,
        totalPrice: String
    ) {
        viewModelScope.launch {
            _sportsState.value = _sportsState.value.copy(isLoading = true)

            repository.bookTicket(matchId, ticketType, quantity, totalPrice).fold(
                onSuccess = {
                    _sportsState.value = _sportsState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _sportsState.value = _sportsState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }
}
