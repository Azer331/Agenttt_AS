package com.tunisietelecom.agent.data.repository

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferences @Inject constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        "tt_agent_prefs", Context.MODE_PRIVATE
    )

    fun saveToken(token: String) {
        prefs.edit().putString("auth_token", token).apply()
    }

    fun getToken(): String? = prefs.getString("auth_token", null)

    fun isLoggedIn(): Boolean = !getToken().isNullOrEmpty()

    fun saveUserData(user: com.tunisietelecom.agent.data.models.User) {
        prefs.edit()
            .putString("user_id", user.id)
            .putString("user_email", user.email)
            .putString("user_first_name", user.firstName)
            .putString("user_last_name", user.lastName)
            .putString("agent_id", user.agentId)
            .putString("user_role", user.role.name)
            .putString("department", user.department)
            .putBoolean("is_active", user.isActive)
            .apply()
    }

    fun clearUserData() {
        prefs.edit().clear().apply()
    }
}