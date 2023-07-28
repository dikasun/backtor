package com.andikas.models.response.personal

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceResponse(
    var id: Long,
    var startYear: String,
    var endYear: String,
    var institution: String,
    var role: String,
    var description: String
)
