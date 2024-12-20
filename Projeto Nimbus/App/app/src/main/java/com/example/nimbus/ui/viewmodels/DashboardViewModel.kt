package com.example.nimbus.ui.viewmodels

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.domain.StatItem
import com.example.nimbus.domain.Team
import com.example.nimbus.dto.Game.LastGameDTO
import com.example.nimbus.dto.Game.NextGameDTO
import com.example.nimbus.utils.SharedPreferencesManager
import com.example.nimbus.utils.calcMapAvg
import com.example.nimbus.utils.getLastConfirmedGame
import com.example.nimbus.utils.getNextConfirmedGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.UUID

data class DashboardUiState(
    val statsItem: List<StatItem> = emptyList(),
    val isLoading: Boolean = true,
    val nextGame: NextGameDTO? = null,
    val lastGame: LastGameDTO? = null
)

class DashboardViewModel(
    private val sharedPrefManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
): ViewModel() {
    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private val graphsApi = RetrofitService.getGraphApi(sharedPrefManager.getAuthToken())
    private val gamesApi = RetrofitService.getGamesApi(sharedPrefManager.getAuthToken())
    private val teamsApi = RetrofitService.getTeamsApi(sharedPrefManager.getAuthToken())

    val teamId = globalViewModel.getSelectedTeam()?.id

    init {
        reload()
    }

    fun reload() {
        viewModelScope.launch {
            val wins = async { fetchWinsByTeam() }
            val pointsDivision = async { fetchPointsDivision() }
            val pointsPerGame = async { fetchPointsPerGame() }
            val games = async { fetchGames() }

            wins.await()
            pointsDivision.await()
            pointsPerGame.await()
            games.await()

            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    @SuppressLint("DefaultLocale")
    private fun fetchPointsDivision() {
        viewModelScope.launch {
            if(teamId != null) {
                try {
                    val response = graphsApi.getPointsDivision(teamId)

                    if(response.isSuccessful && response.body()?.data != null) {
                        val item1 = StatItem("% de pontos de 2", String.format("%.1f", response.body()?.data!!.twoPoints))
                        val item2 = StatItem("% de pontos de 3", String.format("%.1f", response.body()?.data!!.threePoints))

                        val newList = mutableListOf<StatItem>()
                        newList.addAll(_uiState.value.statsItem)

                        newList.add(item1)
                        newList.add(item2)

                        _uiState.value = _uiState.value.copy(
                            statsItem = newList
                        )
                    }
                    else {
                        Log.e("PointsDivision", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
                catch (e: Exception) {
                    Log.e("PointsDivision", "Erro na requisição", e)
                }
            }
        }
    }

    private fun fetchWinsByTeam() {
        viewModelScope.launch {
            if(teamId != null) {
                try {
                    val response = graphsApi.getWinsByTeam(teamId)

                    if(response.isSuccessful && response.body()?.data != null) {
                        val item = StatItem("Vitórias", response.body()?.data!!.wins.toString())

                        val newList = mutableListOf<StatItem>()
                        newList.addAll(_uiState.value.statsItem)
                        newList.add(item)

                        _uiState.value = _uiState.value.copy(
                            statsItem = newList
                        )
                    }
                    else {
                        Log.e("Wins", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
                catch (e: Exception) {
                    Log.e("Wins", "Erro na requisição", e)
                }
            }
        }
    }

    private fun fetchPointsPerGame() {
        viewModelScope.launch {
            if(teamId != null) {
                try {
                    val response = graphsApi.getPointsPerGame(teamId)

                    if(response.isSuccessful && response.body()?.data != null) {
                        val avg = calcMapAvg(response.body()!!.data)
                        val item = StatItem("Média de pontos", avg.toString())

                        val newList = mutableListOf<StatItem>()
                        newList.addAll(_uiState.value.statsItem)
                        newList.add(item)

                        _uiState.value = _uiState.value.copy(
                            statsItem = newList
                        )
                    }
                    else {
                        Log.e("PointsPerGame", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
                catch(e: Exception) {
                    Log.e("PointsPerGame", "Erro na requisição", e)
                }
            }
        }
    }

    private fun fetchGames() {
        viewModelScope.launch {
            if(teamId != null) {
                try {
                    val response = gamesApi.getGamesByTeam(teamId)

                    if(response.isSuccessful && response.body()?.data != null) {
                        Log.i("NextGame (All)", "${response.body()?.data}")
                        val nextGame = response.body()!!.data.getNextConfirmedGame()
                        val lastGame = response.body()!!.data.getLastConfirmedGame()

                        Log.i("NextGame (Single)", "$nextGame")
                        Log.i("LastGame (Single)", "$lastGame")

                        if (nextGame != null) {
                            val nextAdversary = fetchAdversary(nextGame.getAdversary(teamId))

                            if(nextAdversary != null) {
                                val nextGameDTO = NextGameDTO(
                                    adversaryName = nextAdversary.name,
                                    adversaryPicture = nextAdversary.picture,
                                    date = nextGame.getGameDate()
                                )

                                _uiState.value = _uiState.value.copy(nextGame = nextGameDTO)
                            }
                        }

                        if(lastGame != null) {
                            val lastAdversary = fetchAdversary(lastGame.getAdversary(teamId))

                            if(lastAdversary != null) {
                                val lastGameDTO = LastGameDTO(
                                    adversaryName = lastAdversary.name,
                                    gameResult = lastGame.gameResult!!
                                )

                                _uiState.value = _uiState.value.copy(lastGame = lastGameDTO)
                            }
                        }
                    }
                    else {
                        Log.e("NextGame (All)", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
                catch(e: Exception) {
                    Log.e("NextGame (All)", "Erro na requisição", e)
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
}

class DashboardModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val globalViewModel: GlobalViewModel
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DashboardViewModel(sharedPreferencesManager, globalViewModel) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
