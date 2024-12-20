package com.example.nimbus.api

import com.example.nimbus.api.interfaces.AthletesAPI
import com.example.nimbus.api.interfaces.FileUploadAPI
import com.example.nimbus.api.interfaces.GameResultsAPI
import com.example.nimbus.api.interfaces.GamesAPI
import com.example.nimbus.api.interfaces.GraphsAPI
import com.example.nimbus.api.interfaces.InjuryAPI
import com.example.nimbus.api.interfaces.TeamsAPI
import com.example.nimbus.api.interfaces.TrainingAPI
import com.example.nimbus.api.interfaces.UserAPI
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

private fun okHttpClient(token: String): OkHttpClient {
    return OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response {
                val request: Request = chain.request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
                return chain.proceed(request)
            }
        })
        .build()
}

object RetrofitService {
    //val BASE_URL = "http://192.168.15.108:8080/" //ip casa
    //val BASE_URL = "http://192.168.77.1:8080/"
    val BASE_URL = "http://54.173.73.226:8080/"

    fun getRetrofit(token: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient(token))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getTeamsApi(token: String): TeamsAPI = getRetrofit(token).create(TeamsAPI::class.java)
    fun getAthletesApi(token: String): AthletesAPI = getRetrofit(token).create(AthletesAPI::class.java)
    fun getGamesApi(token: String): GamesAPI = getRetrofit(token).create(GamesAPI::class.java)
    fun getGameResultsApi(token: String): GameResultsAPI = getRetrofit(token).create(GameResultsAPI::class.java)
    fun getUsersApi(token: String): UserAPI = getRetrofit(token).create(UserAPI::class.java)
    fun getGraphApi(token: String): GraphsAPI = getRetrofit(token).create(GraphsAPI::class.java)
    fun getTrainingApi(token: String): TrainingAPI = getRetrofit(token).create(TrainingAPI::class.java)
    fun getFilesApi(token: String): FileUploadAPI = getRetrofit(token).create(FileUploadAPI::class.java)
    fun getInjuryApi(token: String): InjuryAPI = getRetrofit(token).create(InjuryAPI::class.java)

}