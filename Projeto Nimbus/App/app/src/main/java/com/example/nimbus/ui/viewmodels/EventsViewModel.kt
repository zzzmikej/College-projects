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
import com.example.nimbus.domain.Game
import com.example.nimbus.domain.StatItem
import com.example.nimbus.dto.Game.GameCreateDTO
import com.example.nimbus.dto.Game.GameWithTeamDTO
import com.example.nimbus.utils.SharedPreferencesManager
import com.example.nimbus.utils.getConfirmedGames
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

data class EventsUiState(
    val isLoading: Boolean = true,
    val games: List<GameWithTeamDTO> = emptyList(),
    val teams: List<Team> = emptyList(),
    val date: LocalDate? = null,
    val endTime: String = "",
    val startTime: String = "",
    val local: String = "",
    val selectedTeam: Team? = null
)

class EventsViewModel(
    private val sharedPrefManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(EventsUiState())
    val uiState: StateFlow<EventsUiState> = _uiState.asStateFlow()

    private val gamesApi = RetrofitService.getGamesApi(sharedPrefManager.getAuthToken())
    private val teamsApi = RetrofitService.getTeamsApi(sharedPrefManager.getAuthToken())
    private val trainingApi = RetrofitService.getTrainingApi(sharedPrefManager.getAuthToken())

    private val teamId = globalViewModel.getSelectedTeam()?.id

    init {
        viewModelScope.launch {
            fetchGames()
            fetchTeams()
        }
    }

    fun onSelectTeam(team: Team) {
        _uiState.value = _uiState.value.copy(selectedTeam = team)
    }

    fun onDateChange(newDate: LocalDate) {
        _uiState.value = _uiState.value.copy(date = newDate)
    }

    fun onChangeStartTime(newStartTime: String) {
        _uiState.value = _uiState.value.copy(startTime = newStartTime)
    }

    fun onChangeEndTime(newEndTime: String) {
        _uiState.value = _uiState.value.copy(endTime = newEndTime)
    }

    fun onLocalChange(newLocal: String) {
        _uiState.value = _uiState.value.copy(local = newLocal)
    }

    private fun fetchGames() {
        viewModelScope.launch {
            if(teamId != null) {
                try {
                    val response = gamesApi.getGamesByTeam(teamId)

                    if(response.isSuccessful && response.body()?.data != null) {
                        Log.i("Events - Games", "${response.body()?.data}")

                        val games = response.body()!!.data

                        games.forEach {
                            val adversary = fetchAdversary(it.getAdversary(teamId))

                            if(adversary != null) {
                                val gameWithAdversary = GameWithTeamDTO(
                                    it.id,
                                    it.confirmed,
                                    it.inicialDateTime,
                                    it.finalDateTime,
                                    it.local,
                                    it.challenger,
                                    it.challenged,
                                    it.gameResult,
                                    adversary.name
                                )

                                val newList = mutableListOf<GameWithTeamDTO>()
                                newList.addAll(_uiState.value.games)
                                newList.add(gameWithAdversary)

                                _uiState.value = _uiState.value.copy(games = newList)
                            }
                        }
                    }
                }
                catch (e: Exception) {
                    Log.e("Games", "Erro na requisição", e)
                }
                finally {
                    _uiState.value = uiState.value.copy(isLoading = false)
                }
            }
        }
    }

    suspend fun fetchAdversary(id: UUID): Team? {
        return withContext(Dispatchers.IO) {
            try {
                val response = teamsApi.getTeam(id)

                if (response.isSuccessful && response.body()?.data != null) {
                    val team = response.body()?.data!!
                    Log.i("Adversary", "$team")
                    //_uiState.value = _uiState.value.copy(adversary = team)
                    team
                } else {
                    Log.i("Adversary", "Erro na resposta: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                Log.e("Adversary", "Erro na requisição", e)
                null
            }
        }
    }

    suspend fun postGame() {
        viewModelScope.launch {
            val game = GameCreateDTO(
                challenger = globalViewModel.getSelectedTeam()?.id.toString(),
                challenged = "c0d06eb7-2767-4fd9-9662-1fe08150bc89",
                inicialDateTime = combineDateAndTime(uiState.value.date!!, uiState.value.startTime)!!,
                finalDateTime = combineDateAndTime(uiState.value.date!!, uiState.value.endTime)!!
            )

            val postGame = listOf(game)
            Log.i("Events", "Jogo a ser cadastrado: $postGame")

            try {
                val response = gamesApi.post(game)

                if(response.isSuccessful) {
                    val gameResponse = response.body()!!.data
                    Log.i("Events", "Jogo cadastrado com sucesso: $gameResponse")
                } else {
                    Log.e("Events", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception) {
                Log.e("Events", "Erro na requisição", e)
            }
        }
    }

    suspend fun fetchTeams() {
        viewModelScope.launch {
            try {
                val response = teamsApi.getAllTeams()

                if(response.isSuccessful && response.body()?.data != null) {
                    val teams = response.body()!!.data.filter { it.id != globalViewModel.getSelectedTeam()?.id }
                    _uiState.value = _uiState.value.copy(teams = teams)
                    Log.i("Events", "Times obtidos com sucesso: $teams")
                }
                else {
                    Log.i("Events", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception) {
                Log.e("Events", "Erro na requisição", e)
            }
        }
    }


    private fun combineDateAndTime(date: LocalDate, time: String): LocalDateTime? {
        return try {
            val localTime = LocalTime.parse(time)
            LocalDateTime.of(date, localTime)
        } catch (e: Exception) {
            // Se o formato da string de tempo não for válido, retorna nulo
            null
        }
    }
}

class EventsModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return EventsViewModel(sharedPreferencesManager, globalViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}