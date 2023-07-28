package com.andikas.models.request.personal

import kotlinx.serialization.Serializable

@Serializable
data class ProfileBody(
    var picture: String,
    var name: String,
    var role: String,
    var description: String
)
