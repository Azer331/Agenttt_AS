package com.tunisietelecom.agent.data.models

data class Client(
    val id: String,
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
    val subscriptionType: String,
    val status: ClientStatus,
    val joinDate: String,
    val lastContact: String?
)

enum class ClientStatus {
    ACTIVE,
    INACTIVE,
    SUSPENDED,
    PENDING
}
