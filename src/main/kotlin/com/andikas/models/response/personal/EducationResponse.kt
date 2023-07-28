package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class EducationResponse(
    var id: Long,
    var year: String,
    var institution: String,
    var role: String,
    var description: String
)
