package com.example.nimbus.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.domain.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.nimbus.domain.Forecast
import com.example.nimbus.utils.SharedPreferencesManager
import retrofit2.Retrofit
import java.util.UUID

data class PredictionUiState(
    val isLoading: Boolean = true,
    val forecast: Forecast? = null,
    val teams: List<Team> = emptyList(),
    val adversary: Team? = null,
    val selectedPage: Int = 4
)

class PredictionViewModel(
    private val sharedPrefManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(PredictionUiState())
    val uiState: StateFlow<PredictionUiState> = _uiState.asStateFlow()

    private val teamsApi = RetrofitService.getTeamsApi(sharedPrefManager.getAuthToken())
    private val team = globalViewModel.getSelectedTeam()

    init {
        fetchAllTeams()
    }

    fun setAdversary(adversary: Team) {
        _uiState.value = _uiState.value.copy(adversary = adversary)
    }

    fun fetchForecast(challengerId: UUID, challengedId: UUID) {
        if(team != null && _uiState.value.adversary != null) {
            val challengerId = globalViewModel.getSelectedTeam()?.id
            val challengedId = _uiState.value.adversary?.id

            if(challengedId != null && challengerId != null) {
                viewModelScope.launch {
                    try {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                        val response = teamsApi.generateForecast(challengerId, challengedId)

                        if(response.isSuccessful && response.body() != null) {
                            _uiState.value = response.body()?.let {
                                _uiState.value.copy(
                                    forecast = it.data
                                )
                            }!!

                            _uiState.value = _uiState.value.copy(isLoading = false)
                        }
                        else {
                            Log.d("Prediction", "Erro na resposta: ${response.errorBody()?.string()}")
                        }
                    }
                    catch (e: Exception) {
                        Log.e("Prediction", "Erro na requisição fetchForecast", e)
                    }
                }
            }
        }
    }

    fun fetchAllTeams() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                val response = teamsApi.getAllTeams()

                if(response.isSuccessful && response.body() != null) {
                    _uiState.value = response.body()?.let {
                        _uiState.value.copy(
                            teams = it.data
                        )
                    }!!

                    _uiState.value = _uiState.value.copy(isLoading = false)
                }
                else {
                    Log.d("Prediction", "Erro na resposta fetchAllTeams: ${response.errorBody()?.string()}")
                }

            }
            catch (e: Exception) {
                Log.e("Prediction", "Erro na requisição fetchAllTeams", e)
            }
        }
    }
}

class PredictionModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PredictionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PredictionViewModel(sharedPreferencesManager, globalViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}