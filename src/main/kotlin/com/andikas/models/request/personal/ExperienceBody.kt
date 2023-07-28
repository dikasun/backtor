package com.andikas.models.request.personal

import kotlinx.serialization.Serializable

@Serializable
data class ExperienceBody(
    var startYear: String,
    var endYear: String,
    var institution: String,
    var role: String,
    var description: String
)
