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
import com.example.nimbus.utils.SharedPreferencesManager

data class MyTeamsScreenUiState(
    val isLoading: Boolean = true,
    val teams: List<Team> = emptyList(),
    val error: String? = null
)

class MyTeamsScreenViewModel(
    private val sharedPrefManager: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(MyTeamsScreenUiState())
    val uiState: StateFlow<MyTeamsScreenUiState> = _uiState.asStateFlow()

    init {
        fetchTeams()
    }

    private fun fetchTeams() {
        viewModelScope.launch {
            try {
                val teamsApi = RetrofitService.getTeamsApi(sharedPrefManager.getAuthToken())
                val response = teamsApi.getAllByCoach(sharedPrefManager.getPersonaId())
                Log.i("Resposta - Get All Teams", "$response")
                Log.i("Dados resposta", "${response.body()?.data}")
                if(response.isSuccessful && !response.body()?.data.isNullOrEmpty()) {
                    _uiState.value = response.body()?.data?.let {
                        _uiState.value.copy(
                            teams = it,
                        )
                    }!!
                }
                else {
                    _uiState.value = _uiState.value.copy(
                        error = "Erro na resposta: ${response.errorBody()?.string()}"
                    )
                    Log.d("TeamsAPI", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Falha na requisição: ${e.message}"
                )
                Log.e("TeamsAPI", "Erro na requisição", e)
            }
            finally {
                _uiState.value = uiState.value.copy(isLoading = false)
            }
        }
    }
}

class MyTeamsModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MyTeamsScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MyTeamsScreenViewModel(sharedPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}