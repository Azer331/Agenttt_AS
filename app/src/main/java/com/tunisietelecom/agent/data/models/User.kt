package com.tunisietelecom.agent.data.models

data class User(
    val id: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val agentId: String,
    val role: UserRole,
    val department: String,
    val isActive: Boolean
)

enum class UserRole {
    REGULAR_AGENT,
    SPECIAL_AGENT,
    SUPERVISOR,
    ADMIN
}