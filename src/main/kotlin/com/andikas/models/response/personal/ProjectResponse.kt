package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class ProjectResponse(
    var id: Long,
    var description: String
)
