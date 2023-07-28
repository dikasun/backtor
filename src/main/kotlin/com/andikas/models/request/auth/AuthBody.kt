package com.andikas.models.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthBody(
    val username: String,
    val password: String,
)
