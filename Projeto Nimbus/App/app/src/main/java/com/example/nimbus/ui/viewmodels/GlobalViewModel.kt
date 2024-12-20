package com.example.nimbus.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.nimbus.dto.Team.TeamTransferDTO
import com.example.nimbus.domain.Athlete
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class GlobalUiState(
    val selectedTeam: TeamTransferDTO? = null,
    val selectedAthlete: Athlete? = null
)

class GlobalViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(GlobalUiState())
    val uiState: StateFlow<GlobalUiState> = _uiState.asStateFlow()

    fun selectTeam(team: TeamTransferDTO) { _uiState.value = _uiState.value.copy(selectedTeam = team) }
    fun getSelectedTeam(): TeamTransferDTO? { return _uiState.value.selectedTeam }
    fun removeTeam() { _uiState.value = _uiState.value.copy(selectedTeam = null) }

    fun selectAthlete(athlete: Athlete) { _uiState.value = _uiState.value.copy(selectedAthlete = athlete) }
    fun getSelectedAthlete(): Athlete? { return _uiState.value.selectedAthlete }
    fun removeAthlete() { _uiState.value = _uiState.value.copy(selectedAthlete = null) }
}