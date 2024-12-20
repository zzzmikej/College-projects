package com.example.nimbus.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import java.util.UUID

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_preferences", Context.MODE_PRIVATE)

    fun getAuthToken(): String {
        val token = sharedPreferences.getString("token", null)
        if(!token.isNullOrEmpty()) return token
        return ""
    }

    fun getUsername(): String {
        val username = sharedPreferences.getString("username", null)
        if(!username.isNullOrEmpty()) return username
        return ""
    }

    fun getPersonaId(): UUID {
        val personaId = sharedPreferences.getString("persona_id", null)
        return UUID.fromString(personaId)
    }

    fun getUserId(): String {
        val userId = sharedPreferences.getString("user_id", null)
        if(!userId.isNullOrEmpty()) return userId
        return ""
    }

    fun getEmail(): String {
        val email = sharedPreferences.getString("email", null)
        if(!email.isNullOrEmpty()) return email
        return ""
    }

    fun saveUserData(userId: String?, personaId: String?, username: String?, token: String?, email: String?) {
        with(sharedPreferences.edit()) {
            userId?.let { putString("user_id", it) } ?: Log.e("SharedPreferencesManager", "userId é nulo.")
            personaId?.let { putString("persona_id", it) } ?: Log.e("SharedPreferencesManager", "personaId é nulo.")
            username?.let { putString("username", it) } ?: Log.e("SharedPreferencesManager", "username é nulo.")
            token?.let { putString("token", it) } ?: Log.e("SharedPreferencesManager", "token é nulo.")
            email?.let { putString("email", it) } ?: Log.e("SharedPreferencesManager", "email é nulo.")
            apply()
        }
    }

    fun clearData() {
        with(sharedPreferences.edit()) {
            clear() // Limpa todas as entradas
            apply() // Aplica as mudanças de forma assíncrona
        }
    }
}