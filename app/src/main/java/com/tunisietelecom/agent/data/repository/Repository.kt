package com.tunisietelecom.agent.data.repository

import com.tunisietelecom.agent.data.api.ApiService
import com.tunisietelecom.agent.data.api.LoginRequest
import com.tunisietelecom.agent.data.api.RegisterRequest
import com.tunisietelecom.agent.data.models.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val apiService: ApiService,
    private val userPreferences: UserPreferences
) {

    suspend fun login(email: String, password: String) =
        apiService.login(LoginRequest(email, password))

    suspend fun register(
        email: String, password: String, firstName: String,
        lastName: String, agentId: String, department: String
    ) = apiService.register(
        RegisterRequest(email, password, firstName, lastName, agentId, department)
    )

    suspend fun getOffers() = apiService.getOffers()

    suspend fun getTechnicalInfo() = apiService.getTechnicalInfo()

    fun saveUserToken(token: String) = userPreferences.saveToken(token)

    fun getUserToken() = userPreferences.getToken()

    fun isUserLoggedIn() = userPreferences.isLoggedIn()

    fun logout() = userPreferences.clearUserData()
}
