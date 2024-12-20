package com.example.nimbus.ui.viewmodels

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.nimbus.api.RetrofitService
import com.example.nimbus.dto.User.UserLoginDTO
import com.example.nimbus.dto.User.UserLoginResponseDTO
import com.example.nimbus.ui.screens.MyTeamsScreen
import com.example.nimbus.utils.SharedPreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val userData: UserLoginResponseDTO? = null,
    val error: String? = null,
    val isLoading: Boolean = false
)

class LoginViewModel(
    private val sharedPrefManager: SharedPreferencesManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(context: Context, loginParam: UserLoginDTO?) {
        viewModelScope.launch {
            try {
                val loginDTO = if(loginParam == null) UserLoginDTO(_uiState.value.email, _uiState.value.password) else loginParam
                val userApi = RetrofitService.getUsersApi(sharedPrefManager.getAuthToken())
                Log.i("Login", sharedPrefManager.getAuthToken())
                val response = userApi.login(loginDTO)

                Log.i("Login", "Resposta da API: $response")

                if(response.isSuccessful && response.body()?.data != null) {
                    _uiState.value = _uiState.value.copy(userData = response.body()?.data)

                    Log.i("Login", "Login realizado com sucesso: ${_uiState.value.userData}")

                    saveUserData(_uiState.value.userData)
                    context.startActivity(Intent(context, MyTeamsScreen::class.java))
                }
                else {
                    _uiState.value = _uiState.value.copy(
                        error = "Erro na resposta: ${response.errorBody()?.string()}"
                    )
                    Log.e("Login", "Erro na resposta: ${response.errorBody()?.string()}")
                }
            }
            catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Falha na requisição: ${e.message}"
                )
                Log.e("TeamsAPI", "Erro na requisição", e)
            }
            finally {

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

    fun onEmailChange(text: String) { _uiState.value = _uiState.value.copy( email = text ) }
    fun onPasswordChange(text: String) { _uiState.value = _uiState.value.copy( password = text ) }
}

class LoginModelFactory(
    private val sharedPreferencesManager: SharedPreferencesManager
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LoginViewModel(sharedPreferencesManager) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}