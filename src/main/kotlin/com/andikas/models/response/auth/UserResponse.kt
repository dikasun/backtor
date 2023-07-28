package com.andikas.models.response.auth

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val id: Long = 0,
    val username: String,
    val password: String,
    val salt: String,
)