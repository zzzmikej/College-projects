package com.example.nimbus.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.domain.Team
import com.example.nimbus.dto.Team.TeamCreateDTO
import com.example.nimbus.dto.User.UserCreateDTO
import com.example.nimbus.dto.User.UserIdsDTO
import com.example.nimbus.dto.User.UserLoginDTO
import com.example.nimbus.dto.User.UserLoginResponseDTO
import com.example.nimbus.ui.screens.MyTeamsScreen
import com.example.nimbus.utils.SharedPreferencesManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.UUID

data class RegisterUiState(
    val user: UserIdsDTO? = null,
    val team: Team? = null,
    val userData: UserCreateDTO? = null,
    val step: Int = 0,
    val loginResponse: UserLoginResponseDTO? = null
)

class RegisterViewModel(
    private val sharedPrefManager: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    val usersApi = RetrofitService.getUsersApi(sharedPrefManager.getAuthToken())
    val fileUploadApi = RetrofitService.getFilesApi(sharedPrefManager.getAuthToken())

    //init {
    //    if(sharedPrefManager.getPersonaId().toString().isNotEmpty()) {
    //        val userId = UserIdsDTO(personaId = sharedPrefManager.getPersonaId(), userId = UUID.fromString(sharedPrefManager.getUserId()))
    //        _uiState.value = _uiState.value.copy(user = userId)
    //    }
    //}

    fun setStep(step: Int) {
        _uiState.value = _uiState.value.copy(step = step)
    }

    fun setUserData(userData: UserCreateDTO) {
        _uiState.value = _uiState.value.copy(userData = userData)
    }

    fun login(context: Context) {
        viewModelScope.launch {
            try {
                val user = _uiState.value.userData
                if(user?.email != null && user?.password != null) {
                    val loginDTO = UserLoginDTO(user.email, user.password)
                    val userApi = RetrofitService.getUsersApi(sharedPrefManager.getAuthToken())
                    val response = userApi.login(loginDTO)

                    if(response.isSuccessful && response.body()?.data != null) {
                        _uiState.value = _uiState.value.copy(loginResponse = response.body()?.data)
                        saveUserData(_uiState.value.loginResponse)
                    }
                    else {
                        Log.e("Login", "Erro na resposta: ${response.errorBody()?.string()}")
                    }
                }
            }
            catch (e: Exception) {
                Log.e("TeamsAPI", "Erro na requisição", e)
            }
            finally {

            }
        }
    }

    suspend fun postUser(userData: UserCreateDTO): UserIdsDTO? {
        val mockCoach = userData.coach?.copy(birthDate = "2004-12-15")
        val mockUser = userData.copy(coach = mockCoach)

        return try {
            val response = usersApi.post(mockUser)
            if (response.isSuccessful) {
                val user = response.body()?.data
                Log.i("Register", "Usuário criado com sucesso: $user")
                _uiState.value = _uiState.value.copy(user = user)
                user
            } else {
                Log.e("Register", "Erro na resposta do usuário: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Register", "Erro na requisição de usuário: $e")
            null
        }
    }

    suspend fun postTeam(team: TeamCreateDTO): Team? {
        val teamsApi = RetrofitService.getTeamsApi(sharedPrefManager.getAuthToken())

        Log.i("Register", "Criando time: $team")
        return try {
            val response = teamsApi.postTeam(team)
            if (response.isSuccessful && response.body()?.data != null) {
                val teamResponse = response.body()?.data
                Log.i("Register", "Time criado com sucesso $teamResponse")
                _uiState.value = _uiState.value.copy(team = teamResponse)
                teamResponse
            } else {
                Log.e("Register", "Erro na resposta do time: ${response.errorBody()?.string()}")
                null
            }
        } catch (e: Exception) {
            Log.e("Register", "Erro na requisição de time: $e")
            null
        }
    }

    suspend fun uploadFile(file: File, entity: UUID) {
        viewModelScope.launch {
            val requestFile = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            val filePart = MultipartBody.Part.createFormData("file", file.name, requestFile)

            try {
                val response = fileUploadApi.postImage(entity, filePart)
                if(response.isSuccessful) {
                    Log.i("Register", "Arquivo enviado com sucesso: ${response.body()}")
                }
            }
            catch (e: Exception) {
                Log.e("Register", "Erro na requisição:", e)
            }
        }
    }

    fun saveUserData(userData: UserLoginResponseDTO?) {
        userData?.let {
            sharedPrefManager.saveUserData(
                userId = it.userId.toString(),
                personaId = it.personaId.toString(),
                username = it.username,
                token = it.token,
                email = it.email
            )
        }
    }
}

class RegisterModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(sharedPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}