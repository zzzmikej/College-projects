package com.example.nimbus.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.domain.Athlete
import com.example.nimbus.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AthleteInfoUiState(
    val athlete: Athlete? = null,
    val isLoading: Boolean = false
)

class AthleteInfoViewModel(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(AthleteInfoUiState())
    val uiState: StateFlow<AthleteInfoUiState> = _uiState.asStateFlow()

    init {
        reload()
    }

    fun reload() {
        fetchAthleteDesc()
    }

    private fun fetchAthleteDesc() {
        viewModelScope.launch {
            try {
                val athleteAPI = RetrofitService.getAthletesApi(sharedPreferencesManager.getAuthToken())
                val response = globalViewModel.getSelectedAthlete()?.id?.let {
                    athleteAPI.getAthlete(
                        it
                    )
                }
                Log.i("Resposta - Get Athlete", "$response")

                if(response != null && response.isSuccessful && response.body()?.data != null) {
                    _uiState.value = response.body()?.data?.let {
                        _uiState.value.copy(
                            athlete = it
                        )
                    }!!
                    Log.i("AthleteInfo", "Info do atleta obtidos com sucesso: ${response.body()}")
                }
                else {
                    if (response != null) {
                        Log.e("Login", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
            }
            catch(e: Exception) {
                Log.e("TeamsAPI", "Erro na requisição", e)
            }
            finally {
                _uiState.value = _uiState.value.copy(isLoading = false)
            }
        }
    }
}

class AthleteINfoModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AthleteInfoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AthleteInfoViewModel(sharedPreferencesManager, globalViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
