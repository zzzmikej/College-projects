package com.example.nimbus.ui.viewmodels

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.domain.Injury
import com.example.nimbus.dto.Athlete.AthleteIdDTO
import com.example.nimbus.dto.Injury.InjuryCreateDTO
import com.example.nimbus.dto.Injury.InjuryGetDTO
import com.example.nimbus.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.UUID

data class AthleteInjuryUiState(
    val typeText: String = "",
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now()
)

class AthleteInjuryScreenViewModel(
    private val globalViewModel: GlobalViewModel,
    private val sharedPrefManager: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(AthleteInjuryUiState())
    val uiState: StateFlow<AthleteInjuryUiState> = _uiState.asStateFlow()

    val injuryApi = RetrofitService.getInjuryApi(sharedPrefManager.getAuthToken())

    fun postInjury() {
        val athleteId = AthleteIdDTO(globalViewModel.uiState.value.selectedAthlete!!.id)

        val injury = InjuryCreateDTO(
            _uiState.value.startDate.toString(),
            _uiState.value.endDate.toString(),
            _uiState.value.typeText,
            athleteId
        )
        viewModelScope.launch {
            try {
                val response = injuryApi.postInjury(injury)
                if(response.isSuccessful) {
                    val injury = response.body()?.data
                    Log.i("AthleteInjury", "Lesão cadastrada com sucesso: $injury")

                    val injuries = mutableListOf<InjuryGetDTO>()
                    globalViewModel.getSelectedAthlete()!!.injuries?.let { injuries.addAll(it) }
                    injuries.add(injury!!)
                    Log.i("AthleteInjury", "Lesões do atleta: $injuries")

                    val athlete = globalViewModel.getSelectedAthlete()!!.copy(injuries = injuries)
                    Log.i("AthleteInjury", "Atleta atualizado: $athlete")

                    globalViewModel.selectAthlete(athlete)
                }
                else {
                    Log.e("AthleteInjury", "Erro ${response.code()}: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception) {
                Log.e("AthleteInjury", "Erro na requisição", e)
            }
        }
    }

    fun onTypeChange(text: String) {
        _uiState.value = _uiState.value.copy(typeText = text)
    }

    fun onStartDateChange(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(startDate = newDate)
    }

    fun onEndDateChange(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(endDate = newDate)
    }
}

class AthleteInjuryModelFactory(
    private val globalViewModel: GlobalViewModel,
    private val sharedPrefManager: SharedPreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AthleteInjuryScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AthleteInjuryScreenViewModel(globalViewModel, sharedPrefManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
