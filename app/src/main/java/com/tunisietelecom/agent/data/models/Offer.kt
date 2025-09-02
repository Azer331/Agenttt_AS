package com.tunisietelecom.agent.data.models

data class Offer(
    val id: String,
    val title: String,
    val description: String,
    val price: String,
    val validUntil: String,
    val category: String,
    val isActive: Boolean,
    val imageUrl: String?
)